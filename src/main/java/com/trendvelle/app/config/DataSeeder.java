package com.trendvelle.app.config;

import com.trendvelle.app.model.*;
import com.trendvelle.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component @RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreReviewRepository storeReviewRepository;
    private final ProductReviewRepository productReviewRepository;

    @Override
    public void run(String... args) {
        if(!userRepository.existsByEmail("admin@trendvelle.com"))
            userRepository.save(User.builder().fullName("Admin").email("admin@trendvelle.com").password(passwordEncoder.encode("admin123")).phone("09000000000").role(User.Role.ADMIN).status(User.AccountStatus.ACTIVE).build());

        User user1=null;
        if(!userRepository.existsByEmail("maria@example.com"))
            user1=userRepository.save(User.builder().fullName("Maria Gonzales").email("maria@example.com").password(passwordEncoder.encode("password")).phone("09171234567").role(User.Role.USER).status(User.AccountStatus.ACTIVE).build());
        else user1=userRepository.findByEmail("maria@example.com").orElse(null);

        if(!userRepository.existsByEmail("jose@example.com"))
            userRepository.save(User.builder().fullName("Jose Reyes").email("jose@example.com").password(passwordEncoder.encode("password")).phone("09181234567").role(User.Role.USER).status(User.AccountStatus.ACTIVE).build());
        if(!userRepository.existsByEmail("ana@example.com"))
            userRepository.save(User.builder().fullName("Ana Lim").email("ana@example.com").password(passwordEncoder.encode("password")).phone("09191234567").role(User.Role.USER).status(User.AccountStatus.ACTIVE).build());

        Category tops=getOrCreate("Tops","👕");
        Category bottoms=getOrCreate("Bottoms","👖");
        Category footwear=getOrCreate("Footwear","👟");
        Category bags=getOrCreate("Bags","👜");
        Category dresses=getOrCreate("Dresses","👗");
        Category outerwear=getOrCreate("Outerwear","🧥");
        getOrCreate("Accessories","🎀");

        if(productRepository.count()==0){
            List<Product> products=List.of(
                Product.builder().name("Classic White Tee").description("Premium cotton blend, relaxed fit. Perfect for everyday wear. Machine washable.").price(new BigDecimal("350")).stockQuantity(50).sizes("XS,S,M,L,XL").icon("👕").category(tops).active(true).build(),
                Product.builder().name("Slim Fit Jeans").description("High-quality denim with stretch fabric. Slim fit through hips and thighs.").price(new BigDecimal("890")).stockQuantity(30).sizes("28,30,32,34,36").icon("👖").category(bottoms).active(true).build(),
                Product.builder().name("Canvas Sneakers").description("Classic canvas upper with rubber sole. Versatile design for any outfit.").price(new BigDecimal("1200")).stockQuantity(25).sizes("38,39,40,41,42,43").icon("👟").category(footwear).active(true).build(),
                Product.builder().name("Floral Sundress").description("Lightweight chiffon with floral print. Perfect for warm weather.").price(new BigDecimal("750")).stockQuantity(20).sizes("XS,S,M,L").icon("👗").category(dresses).active(true).build(),
                Product.builder().name("Denim Jacket").description("Classic denim jacket with button front and chest pockets.").price(new BigDecimal("1500")).stockQuantity(15).sizes("S,M,L,XL").icon("🧥").category(outerwear).active(true).build(),
                Product.builder().name("Canvas Tote Bag").description("Spacious canvas tote bag, ideal for everyday carry.").price(new BigDecimal("450")).stockQuantity(40).sizes("One Size").icon("👜").category(bags).active(true).build(),
                Product.builder().name("Polo Shirt").description("Classic polo shirt with embroidered logo. Business casual ready.").price(new BigDecimal("580")).stockQuantity(35).sizes("S,M,L,XL,XXL").icon("👔").category(tops).active(true).build(),
                Product.builder().name("Maxi Skirt").description("Flowy maxi skirt with elastic waistband. Perfect for any occasion.").price(new BigDecimal("620")).stockQuantity(22).sizes("XS,S,M,L,XL").icon("👘").category(dresses).active(true).build(),
                Product.builder().name("Cargo Shorts").description("Multi-pocket cargo shorts. Durable fabric for outdoor activities.").price(new BigDecimal("490")).stockQuantity(28).sizes("28,30,32,34,36").icon("🩳").category(bottoms).active(true).build(),
                Product.builder().name("Bucket Hat").description("Trendy bucket hat in soft cotton. UV protection included.").price(new BigDecimal("280")).stockQuantity(60).sizes("One Size").icon("🪣").category(getOrCreate("Accessories","🎀")).active(true).build()
            );
            List<Product> saved=productRepository.saveAll(products);
            if(user1!=null){
                productReviewRepository.save(ProductReview.builder().product(saved.get(0)).user(user1).rating(5).comment("Great quality! The fabric is so soft and comfortable. Will definitely buy again.").build());
                productReviewRepository.save(ProductReview.builder().product(saved.get(1)).user(user1).rating(4).comment("Nice fit. Slightly smaller than expected but overall satisfied.").build());
                storeReviewRepository.save(StoreReview.builder().user(user1).rating(5).comment("Excellent store! Fast delivery and great products. The TrendVelle never disappoints!").build());
                storeReviewRepository.save(StoreReview.builder().user(user1).rating(4).comment("Good experience overall. Products match the photos. Will shop again.").build());
            }
        }
    }

    private Category getOrCreate(String name,String icon){
        return categoryRepository.findByName(name).orElseGet(()->categoryRepository.save(Category.builder().name(name).icon(icon).build()));
    }
}
