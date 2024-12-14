package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dao.*;
import com.spring.websellspringmvc.models.Color;
import com.spring.websellspringmvc.models.Image;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Size;
import com.spring.websellspringmvc.services.image.UploadImageServices;
import com.spring.websellspringmvc.services.state.ProductState;

import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AdminProductServices {
    static int LIMIT = 15;
    ProductDAO productDAO;
    ColorDAO colorDAO;
    ImageDAO imageDAO;
    SizeDAO sizeDAO;
    ProductCardDAO productCardDAO;


    public int addProduct(Product product) {
        List<Product> productList = productDAO.getIdProductByName(product.getName());
        if (!productList.isEmpty()) {
            log.error("AdminProductServices: Không thể thêm sản phẩm cùng tên");
            return 0;
        }
        return productDAO.addProduct(product);
    }

    public void addColor(String[] codeColors, int productId) {
        Color[] colors = new Color[codeColors.length];
        for (int i = 0; i < codeColors.length; i++) {
            colors[i] = new Color();
            colors[i].setCodeColor(codeColors[i]);
            colors[i].setProductId(productId);
        }
        colorDAO.addColors(colors);
    }

    public void addSize(String[] nameSizes, double[] sizePrices, int productId) {
        Size[] sizes = new Size[nameSizes.length];
        for (int i = 0; i < sizes.length; i++) {
            Size size = new Size();
            size.setNameSize(nameSizes[i]);
            size.setSizePrice(sizePrices[i]);
            size.setProductId(productId);

            sizes[i] = size;
        }
        sizeDAO.addSizes(sizes);
    }

    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }

    public void updateColors(Color[] colors, int productId) {
//        update
        List<Color> listColorId = colorDAO.getIdColorByProductId(productId);
        int index = Math.min(listColorId.size(), colors.length);
        for (int i = 0; i < index; i++) {
            colorDAO.updateColor(colors[i], listColorId.get(i).getId());
        }
//       delete
        if (listColorId.size() > index) {
            List<Color> colorsDelete = listColorId.subList(index, listColorId.size());
            List<Integer> listIdDelete = (List<Integer>) colorsDelete.stream().map(Color::getId);
            colorDAO.deleteColorList(listIdDelete);
        }
//       create
        if (listColorId.size() < index) {
//            int update = index - listSizeId.size();
            Color[] colorsAdd = Arrays.copyOfRange(colors, index, colors.length);
            colorDAO.addColors(colorsAdd);
        }
    }

    public void updateSizes(Size[] sizes, int productId) {
//        Lấy ra các id size thuôc về product đó đang có trong cửa hàng
        List<Size> listSizeId = sizeDAO.getIdSizeByProductId(productId);

//       update
        int index = Math.min(listSizeId.size(), sizes.length);
        for (int i = 0; i < index; i++) {
            sizeDAO.updateSize(sizes[i], listSizeId.get(i).getId());
        }

//       delete
        if (listSizeId.size() > index) {
            List<Size> sizesDelete = listSizeId.subList(index, listSizeId.size());
            List<Integer> listIdDelete = (List<Integer>) sizesDelete.stream().map(Size::getId);
            sizeDAO.deleteSizeList(listIdDelete);
        }

//       create
        if (sizes.length > index) {
//            int update = index - listSizeId.size();
            Size[] sizesAdd = Arrays.copyOfRange(sizes, index, sizes.length);
            sizeDAO.addSizes(sizesAdd);
        }
    }


    //    Xóa image theo id
    public void deleteImages(List<Integer> idImages) {
        imageDAO.deleteImages(idImages);
    }

    //    Thêm image
    public void addImages(List<Image> images) {
        imageDAO.addImages(images);
    }


}
