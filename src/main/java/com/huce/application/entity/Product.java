package com.huce.application.entity;

import com.huce.application.model.dto.ChartDTO;
import com.huce.application.model.dto.ProductInfoDTO;
import com.huce.application.model.dto.ShortProductInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SqlResultSetMappings(
        value ={
                @SqlResultSetMapping(
                        name ="productInfoDto",
                        classes =@ConstructorResult(
                                targetClass = ProductInfoDTO.class,
                                columns ={
                                        @ColumnResult(name = "id", type = String.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "slug", type = String.class),
                                        @ColumnResult(name = "price", type = Long.class),
                                        @ColumnResult(name = "views",type = Integer.class),
                                        @ColumnResult(name = "images", type = String.class),
                                        @ColumnResult(name = "total_sold", type = Integer.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "shortProductInfoDTO",
                        classes = @ConstructorResult(
                                targetClass = ShortProductInfoDTO.class,
                                columns = {
                                        @ColumnResult(name = "id", type = String.class),
                                        @ColumnResult(name = "name", type = String.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "productInfoAndAvailableSize",
                        classes = @ConstructorResult(
                                targetClass = ShortProductInfoDTO.class,
                                columns = {
                                        @ColumnResult(name = "id", type = String.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "price", type = Long.class),
                                        @ColumnResult(name = "quantity", type = Long.class),

                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "chartProductDTO",
                        classes = @ConstructorResult(
                                targetClass = ChartDTO.class,
                                columns = {
                                        @ColumnResult(name = "label",type = String.class),
                                        @ColumnResult(name = "value",type = Integer.class)
                                }
                        )
                )
        }
)

@NamedNativeQuery(
        name = "getListNewProducts",
        resultSetMapping = "productInfoDto",
        query = "SELECT p.id, p.name, p.sale_price as price, p.product_view as views, p.slug, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p WHERE p.status = 1 " +
                "AND p.expiry >= NOW() " +
                "order by p.created_at DESC limit ?1"
)
@NamedNativeQuery(
        name = "getListBestSellProducts",
        resultSetMapping = "productInfoDto",
        query = "SELECT p.id, p.name, p.sale_price as price, p.product_view as views, p.slug, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p " +
                "WHERE p.status = 1 " +
                "AND p.expiry >= NOW() " +
                "ORDER BY total_sold DESC LIMIT ?1"
)

@NamedNativeQuery(
        name = "getListViewProducts",
        resultSetMapping = "productInfoDto",
        query = "SELECT p.id, p.name, p.sale_price as price, p.product_view as views, p.slug, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p " +
                "WHERE p.status = 1 " +
                "AND p.expiry >= NOW() " +
                "ORDER BY product_view DESC LIMIT ?1"
)

@NamedNativeQuery(
        name = "getRelatedProducts",
        resultSetMapping = "productInfoDto",
        query = "SELECT p.id, p.name, p.sale_price as price, p.product_view as views, p.slug, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p " +
                "INNER JOIN product_category pc " +
                "ON pc.product_id = p.id " +
                "INNER JOIN category c " +
                "ON c.id = pc.category_id " +
                "WHERE p.status = 1 " +
                "AND p.id != ?1 " +
                "AND pc.category_id " +
                "IN (SELECT category_id FROM product_category pc JOIN category c ON pc.category_id = c.id WHERE pc.product_id = ?1) "+
                "AND p.expiry >= NOW() " +
                "ORDER BY RAND() " +
                "LIMIT ?2"
)
@NamedNativeQuery(
        name = "getAllProduct",
        resultSetMapping = "shortProductInfoDTO",
        query = "SELECT p.id, p.name FROM product p where p.expiry >= NOW()"
)
@NamedNativeQuery(
        name = "getAllBySizeAvailable",
        resultSetMapping = "productInfoAndAvailableSize",
        query = "SELECT p.id, p.name, p.sale_price as price, p.quantity as quantity " +
                "FROM product p "+
                "WHERE p.quantity > 0 and p.expiry > now() "
)
//@NamedNativeQuery(
//        name = "searchProductBySize",
//        resultSetMapping = "productInfoDto",
//        query = "SELECT DISTINCT d.* " +
//                "FROM (" +
//                "SELECT DISTINCT product.id, product.name, product.slug, product.sale_price as price, product.product_view as views, product.total_sold, product.images ->> '$[0]' AS images " +
//                "FROM product " +
//                "INNER JOIN product_category " +
//                "ON product.id = product_category.product_id " +
//                "WHERE product.status = 1 AND product.brand_id IN (?1) AND product_category.category_id IN (?2) " +
//                "AND product.sale_price > ?3 AND product.sale_price < ?4) as d " +
//                "INNER JOIN product_size " +
//                "ON product_size.product_id = d.id " +
//                "WHERE product_size.size IN (?5) " +
//                "LIMIT ?6 "+
//                "OFFSET ?7"
//)
@NamedNativeQuery(
        name = "searchProductAllSize",
        resultSetMapping = "productInfoDto",
        query = "SELECT DISTINCT product.id, product.name, product.slug, product.sale_price as price, product.product_view as views, product.total_sold, product.images ->> '$[0]' AS images " +
                "FROM product " +
                "INNER JOIN product_category " +
                "ON product.id = product_category.product_id " +
                "INNER JOIN product_certification " +
                "ON product.id = product_certification.product_id " +
                "WHERE product.status = 1 AND product.brand_id IN (?1) AND product_category.category_id IN (?2) " +
                "AND product_certification.certification_id IN (?3)" +
                "AND product.sale_price >= ?4 AND product.sale_price <= ?5 " +
                "AND product.expiry >= NOW() " +
                "LIMIT ?6 " +
                "OFFSET ?7"
)
@NamedNativeQuery(
        name = "searchProductByKeyword",
        resultSetMapping = "productInfoDto",
        query = "SELECT DISTINCT p.id, p.name, p.slug, p.sale_price as price, p.product_view as views, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p " +
                "INNER JOIN product_category pc " +
                "ON p.id = pc.product_id " +
                "INNER JOIN category c " +
                "ON c.id = pc.category_id " +
                "INNER JOIN product_certification p_ce " +
                "ON p.id = p_ce.product_id " +
                "INNER JOIN certification ce " +
                "ON ce.id = p_ce.certification_id " +
                "WHERE p.status = 1 AND (p.name LIKE CONCAT('%',:keyword,'%') OR c.name LIKE CONCAT('%',:keyword,'%') OR ce.name LIKE CONCAT('%',:keyword,'%')) " +
                "AND p.expiry >= NOW() " +
                "LIMIT :limit " +
                "OFFSET :offset "
)
@NamedNativeQuery(
        name = "getProductOrders",
        resultSetMapping = "chartProductDTO",
        query = "select p.name as label, sum(o.quantity) as value from product p " +
                "inner join orders o on p.id = o.product_id " +
                "where o.status = 3 and date_format(o.created_at,'%m') = ?1 " +
                "and date_format(o.created_at,'%Y') = ?2 " +
                "group by p.id order by sum(o.quantity) desc "
)

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name",nullable = false,length = 300)
    private String name;
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;
    @Column(name = "price")
    private long price;
    @Column(name = "quantity")
    private Long quantity;
    @Column(name = "sale_price")
    private long salePrice;
    @Column(name = "slug",nullable = false)
    private String slug;
    @Type(type = "json")
    @Column(name = "images",columnDefinition = "json")
    private ArrayList<String> images;
    @Type(type = "json")
    @Column(name = "image_feedback",columnDefinition = "json")
    private ArrayList<String> imageFeedBack;
    @Column(name = "product_view")
    private int view;
    @Column(name = "total_sold")
    private long totalSold;
    @Column(name = "status")
    private int status;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "modified_at")
    private Timestamp modifiedAt;
    @Column(name = "date_of_manufacture")
    private Date dateOfManufacture; //ngày sản xuất
    @Column(name = "expiry")
    private Date expiry; //hạn sử dụng

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns =@JoinColumn(name = "product_id"),
            inverseJoinColumns =@JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "product_certification",
            joinColumns =@JoinColumn(name = "product_id"),
            inverseJoinColumns =@JoinColumn(name = "certification_id")
    )
    private List<Certification> certifications;

//    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
//    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Comment> comments;

//    @OneToMany(mappedBy = "product")
//    private List<Rate> rates;
}
