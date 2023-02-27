package com.huce.application.repository;

import com.huce.application.model.dto.ChartDTO;
import com.huce.application.model.dto.ProductInfoDTO;
import com.huce.application.model.dto.ShortProductInfoDTO;
import com.huce.application.entity.Product;
//import com.vuhien.application.entity.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    //Lấy sản phẩm theo tên
    Product findByName(String name);

    //Lấy tất cả sản phẩm
    @Query(value = "SELECT * FROM product pro right join (SELECT DISTINCT p.* FROM product p " +
            "INNER JOIN product_category pc ON p.id = pc.product_id " +
            "INNER JOIN category c ON c.id = pc.category_id " +
            "INNER JOIN product_certification pce ON p.id = pce.product_id " +
            "INNER JOIN certification ce ON ce.id = pce.certification_id " +
            "WHERE p.id LIKE CONCAT('%',?1,'%') " +
            "AND p.name LIKE CONCAT('%',?2,'%') " +
            "AND c.id LIKE CONCAT('%',?3,'%') " +
            "AND ce.id LIKE CONCAT('%',?4,'%') " +
            "AND p.brand_id LIKE CONCAT('%',?5,'%')) as tb1 on pro.id=tb1.id", nativeQuery = true)
    Page<Product> adminGetListProducts(String id, String name, String category, String certification, String brand, Pageable pageable);

    @Query(value = "SELECT * FROM product pro right join (SELECT DISTINCT p.* FROM product p " +
            "INNER JOIN product_category pc ON p.id = pc.product_id " +
            "INNER JOIN category c ON c.id = pc.category_id " +
            "INNER JOIN product_certification pce ON p.id = pce.product_id " +
            "INNER JOIN certification ce ON ce.id = pce.certification_id " +
            "WHERE p.id LIKE CONCAT('%',?1,'%') " +
            "AND p.name LIKE CONCAT('%',?2,'%') " +
            "AND c.id LIKE CONCAT('%',?3,'%') " +
            "AND ce.id LIKE CONCAT('%',?4,'%') " +
            "AND p.expiry >= NOW() " +
            "AND p.brand_id LIKE CONCAT('%',?5,'%')) as tb1 on pro.id=tb1.id", nativeQuery = true)
    Page<Product> adminGetListProductsSells(String id, String name, String category, String certification, String brand, Pageable pageable);

    @Query(value = "SELECT * FROM product pro right join (SELECT DISTINCT p.* FROM product p " +
            "INNER JOIN product_category pc ON p.id = pc.product_id " +
            "INNER JOIN category c ON c.id = pc.category_id " +
            "INNER JOIN product_certification pce ON p.id = pce.product_id " +
            "INNER JOIN certification ce ON ce.id = pce.certification_id " +
            "WHERE p.id LIKE CONCAT('%',?1,'%') " +
            "AND p.name LIKE CONCAT('%',?2,'%') " +
            "AND c.id LIKE CONCAT('%',?3,'%') " +
            "AND ce.id LIKE CONCAT('%',?4,'%') " +
            "AND datediff(p.expiry , now()) < 5 " +
            "AND p.brand_id LIKE CONCAT('%',?5,'%')) as tb1 on pro.id=tb1.id", nativeQuery = true)
    Page<Product> adminGetListProductsAboutToExpire(String id, String name, String category, String certification, String brand, Pageable pageable);

    @Query(value = "SELECT * FROM product pro right join (SELECT DISTINCT p.* FROM product p " +
            "INNER JOIN product_category pc ON p.id = pc.product_id " +
            "INNER JOIN category c ON c.id = pc.category_id " +
            "INNER JOIN product_certification pce ON p.id = pce.product_id " +
            "INNER JOIN certification ce ON ce.id = pce.certification_id " +
            "WHERE p.id LIKE CONCAT('%',?1,'%') " +
            "AND p.name LIKE CONCAT('%',?2,'%') " +
            "AND c.id LIKE CONCAT('%',?3,'%') " +
            "AND ce.id LIKE CONCAT('%',?4,'%') " +
            "AND p.expiry < NOW() " +
            "AND p.brand_id LIKE CONCAT('%',?5,'%')) as tb1 on pro.id=tb1.id", nativeQuery = true)
    Page<Product> adminGetListProductsNotSold(String id, String name, String category, String certification, String brand, Pageable pageable);

    //Lấy sản phẩm được bán nhiều
    @Query(nativeQuery = true,name = "getListBestSellProducts")
    List<ProductInfoDTO> getListBestSellProducts(int limit);

    //Lấy sản phẩm mới nhất
    @Query(nativeQuery = true,name = "getListNewProducts")
    List<ProductInfoDTO> getListNewProducts(int limit);

    //Lấy sản phẩm được xem nhiều
    @Query(nativeQuery = true,name = "getListViewProducts")
    List<ProductInfoDTO> getListViewProducts(int limit);

    //Lấy sản phẩm liên quan
    @Query(nativeQuery = true, name = "getRelatedProducts")
    List<ProductInfoDTO> getRelatedProducts(String id, int limit);

    //Lấy sản phẩm
    @Query(name = "getAllProduct", nativeQuery = true)
    List<ShortProductInfoDTO> getListProduct();

    //Lấy sản phẩm có sẵn size
    @Query(nativeQuery = true, name = "getAllBySizeAvailable")
    List<ShortProductInfoDTO> getAvailableProducts();

    @Query(value = "SELECT * FROM product WHERE id = ?1 AND quantity > 0 and expiry > now()", nativeQuery = true)
    Product checkProductAndExpiryAvailable(String id);
    //Trừ một sản phẩm đã bán
    @Transactional
    @Modifying
    @Query(value = "UPDATE product SET total_sold = total_sold - ?1 WHERE id = ?2", nativeQuery = true)
    void minusProductTotalSold(Long quantity, String productId);

    //Cộng một sản phẩm đã bán
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update product set total_sold = total_sold + ?1 where id = ?2")
    void plusProductTotalSold(Long quantity, String productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product SET quantity = quantity - ?1 WHERE id = ?2", nativeQuery = true)
    void minusProduct(Long quantity, String productId);

    //Cộng một sản phẩm đã bán
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update product set quantity = quantity + ?1 where id = ?2")
    void plusProduct(Long quantity, String productId);



    //Tìm kiến sản phẩm k theo size
    @Query(nativeQuery = true, name = "searchProductAllSize")
    List<ProductInfoDTO> searchProductAllSize(List<Long> brands, List<Long> categories,List<Long> certifications, long minPrice, long maxPrice, int limit, int offset);

    //Đếm số sản phẩm
    @Query(nativeQuery = true, value = "SELECT COUNT(DISTINCT product.id) " +
            "FROM product " +
            "INNER JOIN product_category " +
            "ON product.id = product_category.product_id " +
            "INNER JOIN product_certification " +
            "ON product.id = product_certification.product_id " +
            "WHERE product.status = 1 AND product.brand_id IN (?1) AND product_category.category_id IN (?2) " +
            "AND product_certification.certification_id IN (?3)" +
            "AND product.sale_price >= ?4 AND product.sale_price <= ?5 ")
    int countProductAllSize(List<Long> brands, List<Long> categories,List<Long> certifications,long minPrice, long maxPrice);

    //Tìm kiến sản phẩm theo tên và tên danh mục
    @Query(nativeQuery = true, name = "searchProductByKeyword")
    List<ProductInfoDTO> searchProductByKeyword(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    //Đếm số sản phẩm
    @Query(nativeQuery = true, value = "SELECT count(DISTINCT product.id) " +
            "FROM product " +
            "INNER JOIN product_category " +
            "ON product.id = product_category.product_id " +
            "INNER JOIN category " +
            "ON category.id = product_category.category_id " +
            "INNER JOIN product_certification " +
            "ON product.id = product_certification.product_id " +
            "INNER JOIN certification " +
            "ON certification.id = product_certification.certification_id " +
            "WHERE product.status = true AND (product.name LIKE CONCAT('%',:keyword,'%') OR category.name LIKE CONCAT('%',:keyword,'%') OR certification.name LIKE CONCAT('%',:keyword,'%')) ")
    int countProductByKeyword(@Param("keyword") String keyword);

    @Query(name = "getProductOrders",nativeQuery = true)
    List<ChartDTO> getProductOrders(Pageable pageable, Integer moth, Integer year);
}
