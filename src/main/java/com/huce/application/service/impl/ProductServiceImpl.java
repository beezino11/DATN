package com.huce.application.service.impl;

import com.github.slugify.Slugify;
import com.huce.application.entity.*;
import com.huce.application.exception.BadRequestException;
import com.huce.application.exception.InternalServerException;
import com.huce.application.exception.NotFoundException;
import com.huce.application.model.dto.DetailProductInfoDTO;
import com.huce.application.model.dto.PageableDTO;
import com.huce.application.model.dto.ProductInfoDTO;
import com.huce.application.model.dto.ShortProductInfoDTO;
import com.huce.application.model.mapper.ProductMapper;
import com.huce.application.model.request.CreateProductRequest;
import com.huce.application.model.request.FilterProductRequest;
import com.huce.application.model.request.UpdateFeedBackRequest;
import com.huce.application.repository.OrderRepository;
import com.huce.application.repository.ProductRepository;
import com.huce.application.repository.PromotionRepository;
import com.huce.application.config.CommonUtil;
import com.huce.application.service.ProductService;
import com.huce.application.service.PromotionService;
import com.huce.application.utils.PageUtil;
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

import static com.huce.application.config.Constants.*;

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
        return productRepository.adminGetListProducts(id, name, category,certification, brand, pageable);
    }

    @Override
    public Page<Product> adminGetListProductsSells(String id, String name, String category, String certification, String brand, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_PRODUCT, Sort.by("created_at").descending());
        return productRepository.adminGetListProductsSells(id, name, category,certification, brand, pageable);
    }

    @Override
    public Page<Product> adminGetListProductsNotSold(String id, String name, String category, String certification, String brand, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_PRODUCT, Sort.by("created_at").descending());
        return productRepository.adminGetListProductsNotSold(id, name, category,certification, brand, pageable);
    }

    @Override
    public Page<Product> adminGetListProductsAboutToExpire(String id, String name, String category, String certification, String brand, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_PRODUCT, Sort.by("created_at").descending());
        return productRepository.adminGetListProductsAboutToExpire(id, name, category,certification, brand, pageable);
    }

    @Override
    public Product createProduct(CreateProductRequest createProductRequest) {
        //Kiểm tra có danh muc
        if (createProductRequest.getCategoryIds().isEmpty()) {
            throw new BadRequestException("Thể loại trống!");
        }

        //Kiểm tra có danh muc
        if (createProductRequest.getCertification_ids().isEmpty()) {
            throw new BadRequestException("Chứng nhận trống!");
        }

        //kiểm tra nhãn hiệu
        if (createProductRequest.getBrandId() == null){
            throw new BadRequestException("Nhãn hiệu trống!");
        }
        //Kiểm tra có ảnh sản phẩm
        if (createProductRequest.getImages().isEmpty()) {
            throw new BadRequestException("Ảnh sản phẩm trống!");
        }
        //Kiểm tra tên sản phẩm trùng
        Product product = productRepository.findByName(createProductRequest.getName());
        if (product != null) {
//            throw new BadRequestException("Tên sản phẩm đã tồn tại trong hệ thống, Vui lòng chọn tên khác!");
            product.setQuantity(product.getQuantity() + createProductRequest.getQuantity());
            product.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            try {
                productRepository.save(product);
            } catch (Exception ex) {
                throw new InternalServerException("Lỗi khi thêm sản phẩm");
            }
        }else {
            product = ProductMapper.toProduct(createProductRequest);
            //Sinh id
            String id = RandomStringUtils.randomAlphanumeric(6);
            product.setId(id);
            product.setTotalSold(0);
            product.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            try {
                productRepository.save(product);
            } catch (Exception ex) {
                throw new InternalServerException("Lỗi khi thêm sản phẩm");
            }
        }
        return product;
    }

    @Override
    public void updateProduct(CreateProductRequest createProductRequest, String id) {
        //Kiểm tra sản phẩm có tồn tại
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException("Không tìm thấy sản phẩm!");
        }

        //Kiểm tra tên sản phẩm có tồn tại
        Product rs = productRepository.findByName(createProductRequest.getName());
        if (rs != null) {
            if (!createProductRequest.getId().equals(rs.getId()))
                throw new BadRequestException("Tên sản phẩm đã tồn tại trong hệ thống, Vui lòng chọn tên khác!");
        }

        //Kiểm tra có danh muc
        if (createProductRequest.getCategoryIds().isEmpty()) {
            throw new BadRequestException("Danh mục trống!");
        }

        //Kiểm tra có ảnh sản phẩm
        if (createProductRequest.getImages().isEmpty()) {
            throw new BadRequestException("Ảnh sản phẩm trống!");
        }
        //kiểm tra nhãn hiệu
        if (createProductRequest.getBrandId() == null){
            throw new BadRequestException("Nhãn hiệu trống!");
        }

        if (createProductRequest.getCertification_ids().isEmpty()) {
            throw new BadRequestException("Nhãn hiệu trống!");
        }

        if (createProductRequest.getDateOfManufacture() == null){
            throw new BadRequestException("Ngày sản xuất trống!");
        }

        if (createProductRequest.getExpiry() == null){
            throw new BadRequestException("Hạn sử dụng trống!");
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
            throw new InternalServerException("Có lỗi khi sửa sản phẩm!");
        }
    }

    @Override
    public Product getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException("Không tìm thấy sản phẩm trong hệ thống!");
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
            throw new NotFoundException("Sản phẩm không tồn tại");
        }

        // If have order, can't delete
        int countOrder = orderRepository.countByProductId(id);
        if (countOrder > 0) {
            throw new BadRequestException("Sản phẩm đã được đặt hàng không thể xóa");
        }

        try {
            productRepository.deleteById(id);
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new InternalServerException("Lỗi khi xóa sản phẩm");
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
            throw new NotFoundException("Sản phẩm không tồn tại");
        }
        Product product = rs.get();

        if (product.getStatus() != 1) {
            throw new NotFoundException("Sản phâm không tồn tại");
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

        //Cộng sản phẩm xem
        product.setView(product.getView() + 1);
        productRepository.save(product);

        //Kiểm tra có khuyến mại
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
            throw new NotFoundException("Sản phẩm không tồn tại");
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
        //Kiểm tra có khuyến mại
        Promotion promotion = promotionService.checkPublicPromotion();
        if (promotion != null) {
            //Tính giá sản phẩm khi có khuyến mại
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

        //Lấy danh sách sản phẩm và tổng số sản phẩm
        int totalItems;
        List<ProductInfoDTO> products;

            //Nếu không có size
            products = productRepository.searchProductAllSize(req.getBrands(), req.getCategories(), req.getCertifications() , req.getMinPrice(), req.getMaxPrice(), LIMIT_PRODUCT_SHOP, pageUtil.calculateOffset());
            totalItems = productRepository.countProductAllSize(req.getBrands(), req.getCategories(), req.getCertifications(), req.getMinPrice(), req.getMaxPrice());

//        Tính tổng số trang
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

        //Lấy danh sách sản phẩm theo key
        List<ProductInfoDTO> products = productRepository.searchProductByKeyword(keyword, LIMIT_PRODUCT_SEARCH, pageInfo.calculateOffset());

        //Lấy số sản phẩm theo key
        int totalItems = productRepository.countProductByKeyword(keyword);

        //Tính số trang
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
            throw new NotFoundException("Sản phẩm không tồn tại");
        }

        Product product = rs.get();
        product.setImageFeedBack(req.getFeedBackImages());
        try {
            productRepository.save(product);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi cập nhật hình ảnh on feet");
        }
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }
}
