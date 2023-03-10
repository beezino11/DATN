package com.wru.application.service.impl;

import com.github.slugify.Slugify;
import com.wru.application.config.CommonUtil;
import com.wru.application.entity.*;
import com.wru.application.exception.BadRequestException;
import com.wru.application.exception.InternalServerException;
import com.wru.application.exception.NotFoundException;
import com.wru.application.model.dto.DetailProductInfoDTO;
import com.wru.application.model.dto.PageableDTO;
import com.wru.application.model.dto.ProductInfoDTO;
import com.wru.application.model.dto.ShortProductInfoDTO;
import com.wru.application.model.mapper.ProductMapper;
import com.wru.application.model.request.CreateProductRequest;
import com.wru.application.model.request.FilterProductRequest;
import com.wru.application.model.request.UpdateFeedBackRequest;
import com.wru.application.repository.OrderRepository;
import com.wru.application.repository.ProductRepository;
import com.wru.application.repository.PromotionRepository;
import com.wru.application.service.ProductService;
import com.wru.application.service.PromotionService;
import com.wru.application.utils.PageUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.wru.application.config.Constants.*;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Page<Product> adminGetListProduct(String id, String name, String category, String certification, String brand, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_PRODUCT, Sort.by("created_at").descending());
        return productRepository.adminGetListProducts(id, name, category, certification, brand, pageable);
    }

    @Override
    public Page<Product> adminGetListProductsSells(String id, String name, String category, String certification, String brand, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_PRODUCT, Sort.by("created_at").descending());
        return productRepository.adminGetListProductsSells(id, name, category, certification, brand, pageable);
    }

    @Override
    public Page<Product> adminGetListProductsNotSold(String id, String name, String category, String certification, String brand, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_PRODUCT, Sort.by("created_at").descending());
        return productRepository.adminGetListProductsNotSold(id, name, category, certification, brand, pageable);
    }

    @Override
    public Page<Product> adminGetListProductsAboutToExpire(String id, String name, String category, String certification, String brand, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_PRODUCT, Sort.by("created_at").descending());
        return productRepository.adminGetListProductsAboutToExpire(id, name, category, certification, brand, pageable);
    }

    @Override
    public Product createProduct(CreateProductRequest createProductRequest) {
        //Ki???m tra c?? danh muc
        if (createProductRequest.getCategoryIds().isEmpty()) {
            throw new BadRequestException("Th??? lo???i tr???ng!");
        }

        //Ki???m tra c?? danh muc
        if (createProductRequest.getCertification_ids().isEmpty()) {
            throw new BadRequestException("Ch???ng nh???n tr???ng!");
        }

        //ki???m tra nh??n hi???u
        if (createProductRequest.getBrandId() == null) {
            throw new BadRequestException("Nh??n hi???u tr???ng!");
        }
        //Ki???m tra c?? ???nh s???n ph???m
        if (createProductRequest.getImages().isEmpty()) {
            throw new BadRequestException("???nh s???n ph???m tr???ng!");
        }
        //Ki???m tra t??n s???n ph???m tr??ng
        Product product = productRepository.findByName(createProductRequest.getName());
        if (product != null) {
//            throw new BadRequestException("T??n s???n ph???m ???? t???n t???i trong h??? th???ng, Vui l??ng ch???n t??n kh??c!");
            product.setQuantity(product.getQuantity() + createProductRequest.getQuantity());
            product.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            try {
                productRepository.save(product);
            } catch (Exception ex) {
                throw new InternalServerException("L???i khi th??m s???n ph???m");
            }
        } else {
            product = ProductMapper.toProduct(createProductRequest);
            //Sinh id
            String id = RandomStringUtils.randomAlphanumeric(6);
            product.setId(id);
            product.setTotalSold(0);
            product.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            try {
                productRepository.save(product);
            } catch (Exception ex) {
                throw new InternalServerException("L???i khi th??m s???n ph???m");
            }
        }
        return product;
    }

    @Override
    public void updateProduct(CreateProductRequest createProductRequest, String id) {
        //Ki???m tra s???n ph???m c?? t???n t???i
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException("Kh??ng t??m th???y s???n ph???m!");
        }

        //Ki???m tra t??n s???n ph???m c?? t???n t???i
        Product rs = productRepository.findByName(createProductRequest.getName());
        if (rs != null) {
            if (!createProductRequest.getId().equals(rs.getId()))
                throw new BadRequestException("T??n s???n ph???m ???? t???n t???i trong h??? th???ng, Vui l??ng ch???n t??n kh??c!");
        }

        //Ki???m tra c?? danh muc
        if (createProductRequest.getCategoryIds().isEmpty()) {
            throw new BadRequestException("Danh m???c tr???ng!");
        }

        //Ki???m tra c?? ???nh s???n ph???m
        if (createProductRequest.getImages().isEmpty()) {
            throw new BadRequestException("???nh s???n ph???m tr???ng!");
        }
        //ki???m tra nh??n hi???u
        if (createProductRequest.getBrandId() == null) {
            throw new BadRequestException("Nh??n hi???u tr???ng!");
        }

        if (createProductRequest.getCertification_ids().isEmpty()) {
            throw new BadRequestException("Nh??n hi???u tr???ng!");
        }

        if (createProductRequest.getDateOfManufacture() == null) {
            throw new BadRequestException("Ng??y s???n xu???t tr???ng!");
        }

        if (createProductRequest.getExpiry() == null) {
            throw new BadRequestException("H???n s??? d???ng tr???ng!");
        }

//        Product result = ProductMapper.toProduct(createProductRequest);
        Product result = product.get();
        result.setName(createProductRequest.getName());
        result.setDescription(createProductRequest.getDescription());
        result.setPrice(createProductRequest.getPrice());
        result.setQuantity(createProductRequest.getQuantity());
        result.setDateOfManufacture(CommonUtil.convertStringToDate(createProductRequest.getDateOfManufacture(), false));
        result.setExpiry(CommonUtil.convertStringToDate(createProductRequest.getExpiry(), false));
        result.setSalePrice(createProductRequest.getSalePrice());
        result.setImages(createProductRequest.getImages());
        result.setImageFeedBack(createProductRequest.getFeedBackImages());
        result.setStatus(createProductRequest.getStatus());
        //Gen slug
        Slugify slug = new Slugify();
        result.setSlug(slug.slugify(createProductRequest.getName()));
        //Brand
        Brand brand = new Brand();
        brand.setId(createProductRequest.getBrandId());
        result.setBrand(brand);
        //Category
        ArrayList<Category> categories = new ArrayList<>();
        for (Integer id1 : createProductRequest.getCategoryIds()) {
            Category category = new Category();
            category.setId(id1);
            categories.add(category);
        }
        result.setCategories(categories);

        ArrayList<Certification> certifications = new ArrayList<>();
        for (Long id2 : createProductRequest.getCertification_ids()) {
            Certification certification = new Certification();
            certification.setId(id2);
            certifications.add(certification);
        }
        result.setCertifications(certifications);
        result.setId(id);
        result.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        try {
            productRepository.save(result);
        } catch (Exception e) {
            throw new InternalServerException("C?? l???i khi s???a s???n ph???m!");
        }
    }

    @Override
    public Product getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException("Kh??ng t??m th???y s???n ph???m trong h??? th???ng!");
        }
        return product.get();
    }

    @Override
    public void deleteProduct(String[] ids) {
        for (String id : ids) {
            productRepository.deleteById(id);
        }
    }

    @Override
    public void deleteProductById(String id) {
        // Check product exist
        Optional<Product> rs = productRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("S???n ph???m kh??ng t???n t???i");
        }

        // If have order, can't delete
        int countOrder = orderRepository.countByProductId(id);
        if (countOrder > 0) {
            throw new BadRequestException("S???n ph???m ???? ???????c ?????t h??ng kh??ng th??? x??a");
        }

        try {
            productRepository.deleteById(id);
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new InternalServerException("L???i khi x??a s???n ph???m");
        }
    }

    @Override
    public List<ProductInfoDTO> getListBestSellProducts() {
        List<ProductInfoDTO> productInfoDTOS = productRepository.getListBestSellProducts(LIMIT_PRODUCT_SELL);
        return checkPublicPromotion(productInfoDTOS);
    }

    @Override
    public List<ProductInfoDTO> getListNewProducts() {
        List<ProductInfoDTO> productInfoDTOS = productRepository.getListNewProducts(LIMIT_PRODUCT_NEW);
        return checkPublicPromotion(productInfoDTOS);

    }

    @Override
    public List<ProductInfoDTO> getListViewProducts() {
        List<ProductInfoDTO> productInfoDTOS = productRepository.getListViewProducts(LIMIT_PRODUCT_VIEW);
        return checkPublicPromotion(productInfoDTOS);
    }

    @Override
    public DetailProductInfoDTO getDetailProductById(String id) {
        Optional<Product> rs = productRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("S???n ph???m kh??ng t???n t???i");
        }
        Product product = rs.get();

        if (product.getStatus() != 1) {
            throw new NotFoundException("S???n ph??m kh??ng t???n t???i");
        }
        DetailProductInfoDTO dto = new DetailProductInfoDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getSalePrice());
        dto.setViews(product.getView());
        dto.setSlug(product.getSlug());
        dto.setTotalSold(product.getTotalSold());
        dto.setDescription(product.getDescription());
        dto.setBrand(product.getBrand());
        dto.setFeedbackImages(product.getImageFeedBack());
        dto.setProductImages(product.getImages());
        dto.setComments(product.getComments());
        dto.setQuantity(product.getQuantity());

        //C???ng s???n ph???m xem
        product.setView(product.getView() + 1);
        productRepository.save(product);

        //Ki???m tra c?? khuy???n m???i
        Promotion promotion = promotionService.checkPublicPromotion();
        if (promotion != null) {
            dto.setCouponCode(promotion.getCouponCode());
            dto.setPromotionPrice(promotionService.calculatePromotionPrice(dto.getPrice(), promotion));
        } else {
            dto.setCouponCode("");
        }
        return dto;

    }

    @Override
    public List<ProductInfoDTO> getRelatedProducts(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException("S???n ph???m kh??ng t???n t???i");
        }
        List<ProductInfoDTO> products = productRepository.getRelatedProducts(id, LIMIT_PRODUCT_RELATED);
        return checkPublicPromotion(products);
    }

    @Override
    public List<ShortProductInfoDTO> getListProduct() {
        return productRepository.getListProduct();
    }

    @Override
    public List<ShortProductInfoDTO> getAvailableProducts() {
        return productRepository.getAvailableProducts();
    }

    @Override
    public boolean checkProductAndExpiryAvailable(String id) {
        Product productSize = productRepository.checkProductAndExpiryAvailable(id);
        if (productSize != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<ProductInfoDTO> checkPublicPromotion(List<ProductInfoDTO> products) {
        //Ki???m tra c?? khuy???n m???i
        Promotion promotion = promotionService.checkPublicPromotion();
        if (promotion != null) {
            //T??nh gi?? s???n ph???m khi c?? khuy???n m???i
            for (ProductInfoDTO product : products) {
                long discountValue = promotion.getMaximumDiscountValue();
                if (promotion.getDiscountType() == DISCOUNT_PERCENT) {
                    long tmp = product.getPrice() * promotion.getDiscountValue() / 100;
                    if (tmp < discountValue) {
                        discountValue = tmp;
                    }
                }
                long promotionPrice = product.getPrice() - discountValue;
                if (promotionPrice > 0) {
                    product.setPromotionPrice(promotionPrice);
                } else {
                    product.setPromotionPrice(0);
                }
            }
        }

        return products;
    }

    @Override
    public PageableDTO filterProduct(FilterProductRequest req) {

        PageUtil pageUtil = new PageUtil(LIMIT_PRODUCT_SHOP, req.getPage());

        //L???y danh s??ch s???n ph???m v?? t???ng s??? s???n ph???m
        int totalItems;
        List<ProductInfoDTO> products;

        //N???u kh??ng c?? size
        products = productRepository.searchProductAllSize(req.getBrands(), req.getCategories(), req.getCertifications(), req.getMinPrice(), req.getMaxPrice(), LIMIT_PRODUCT_SHOP, pageUtil.calculateOffset());
        totalItems = productRepository.countProductAllSize(req.getBrands(), req.getCategories(), req.getCertifications(), req.getMinPrice(), req.getMaxPrice());

//        T??nh t???ng s??? trang
        int totalPages = pageUtil.calculateTotalPage(totalItems);

        return new PageableDTO(checkPublicPromotion(products), totalPages, req.getPage());
    }

    @Override
    public PageableDTO searchProductByKeyword(String keyword, Integer page) {
        // Validate
        if (keyword == null) {
            keyword = "";
        }
        if (page == null) {
            page = 1;
        }

        PageUtil pageInfo = new PageUtil(LIMIT_PRODUCT_SEARCH, page);

        //L???y danh s??ch s???n ph???m theo key
        List<ProductInfoDTO> products = productRepository.searchProductByKeyword(keyword, LIMIT_PRODUCT_SEARCH, pageInfo.calculateOffset());

        //L???y s??? s???n ph???m theo key
        int totalItems = productRepository.countProductByKeyword(keyword);

        //T??nh s??? trang
        int totalPages = pageInfo.calculateTotalPage(totalItems);

        return new PageableDTO(checkPublicPromotion(products), totalPages, page);
    }

    @Override
    public Promotion checkPromotion(String code) {
        return promotionRepository.checkPromotion(code);
    }

    @Override
    public long getCountProduct() {
        return productRepository.count();
    }

    @Override
    public void updatefeedBackImages(String id, UpdateFeedBackRequest req) {
        // Check product exist
        Optional<Product> rs = productRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("S???n ph???m kh??ng t???n t???i");
        }

        Product product = rs.get();
        product.setImageFeedBack(req.getFeedBackImages());
        try {
            productRepository.save(product);
        } catch (Exception ex) {
            throw new InternalServerException("L???i khi c???p nh???t h??nh ???nh on feet");
        }
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }
}
