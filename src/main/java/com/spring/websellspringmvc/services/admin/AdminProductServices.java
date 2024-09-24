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

    public void addImages(List<String> nameImages, int productId) {
        List<Image> imageList = new ArrayList<>();
        for (int i = 0; i < nameImages.size(); i++) {
            Image image = new Image();
            if (nameImages.get(i) == null)
                continue;
            image.setNameImage(nameImages.get(i));
            image.setProductId(productId);
            imageList.add(image);
        }
        imageDAO.addImages(imageList);
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

    public List<Product> filter(List<Integer> listId, int pageNumber) {
        int offset = (pageNumber - 1) * LIMIT;
        List<Product> productList = productCardDAO.pagingAndFilter(listId, offset, LIMIT);
        return productList;
    }

    public int getQuantityPage() {
        double quantityPage = Math.ceil(Double.parseDouble(productCardDAO.getQuantityProduct() + "") / LIMIT);
        return (int) quantityPage;
    }

    public int getQuantityPage(List<Integer> listId) {
        double quantityPage = Math.ceil(Double.parseDouble(productCardDAO.getQuantityProduct(listId) + "") / LIMIT);
        return (int) quantityPage;
    }

    public int getQuantityPage(int limit) {
        double quantityPage = Math.ceil(Double.parseDouble(productCardDAO.getQuantityProduct() + "") / limit);
        System.out.println("quantityPage: " + quantityPage);
        return (int) quantityPage;
    }

    public List<Integer> getProductByName(String name) {
        List<Product> listProduct = productCardDAO.getIdProductByName(name);
        if (listProduct.isEmpty()) return null;
        List<Integer> listId = new ArrayList<>();
        for (Product p : listProduct) {
            listId.add(p.getId());
        }
        return listId;
    }


    public List<Integer> getProductByTimeCreated(Date dateBegin, Date dateEnd) {
        List<Product> listProduct = productCardDAO.getProductByTimeCreated(dateBegin, dateEnd);
        if (listProduct.isEmpty()) return null;
        List<Integer> listId = new ArrayList<>();
        for (Product p :
                listProduct) {
            listId.add(p.getId());
        }
        return listId;
    }

    public List<Product> getProducts(int numberPage) {
        List<Product> productList = productCardDAO.getProducts(numberPage, LIMIT);
        return productList;
    }

    public boolean isContain(Product product) {
        List<Product> productList = productDAO.getIdProductByName(product.getName());
        return !productList.isEmpty();
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

    private List<String> getNameImages(int quantityFromRightToLeft, int productId) {
//        List<Image> imageList = imageDAO.getNameImages(productId);
//        Collections.reverse(imageList);
//
//        List<Image> imageDelete = imageList.subList(0, quantityFromRightToLeft);
//        for (int i = 0; i < imageDelete.size(); i++) {
//            if (keepImageAvailable(imageList, imageDelete.get(i)) > 1) {
//                imageDelete.remove(imageDelete.get(i));
//            }
//        }
//
//        List<String> nameImageList = new ArrayList<>();
//        for (Image img : imageDelete) {
//            nameImageList.add("product_img/" + img.getNameImage());
//        }
//
//        return nameImageList;
        return null;
    }

    public int keepImageAvailable(List<Image> imageList, Image image) {
        int count = 0;
        for (Image img : imageList) {
            if (img.equals(image)) {
                count++;
            }
        }
        return count;
    }

    private List<Integer> getIdImages(int quantityImgDelete, int productId) {
        List<Image> imageList = imageDAO.getIdImages(productId);
        List<Integer> nameImageList = new ArrayList<>();
        for (Image image : imageList) {
            nameImageList.add(image.getId());
        }
        return nameImageList.subList(imageList.size() - quantityImgDelete, imageList.size());
    }

    //    Xóa image theo id
    public void deleteImages(List<Integer> idImages) {
        imageDAO.deleteImages(idImages);
    }

    //    Thêm image
    public void addImages(List<Image> images) {
        imageDAO.addImages(images);
    }

    public void updateImages(UploadImageServices uploadImageServices, Collection<Part> images, int productId) throws Exception {

//        if (quantityImgDelete != 0) {
//            List<String> nameImages = getNameImages(quantityImgDelete, productId);
//            List<Integer> imageId = getIdImages(quantityImgDelete, productId);
//            uploadImageServices.deleteImages(nameImages);//delete in cloud
//            deleteImages(imageId);//delete in db
//        } else {
//            uploadImageServices.addImages(images);//add in cloud
//            addImages(nameImagesAdded, productId);//add in db
//        }
//            List<String> nameImagesAdded = uploadImageServices.getNameImages();
    }

    public void updateVisibility(int productId, ProductState state) {
        productCardDAO.updateVisibility(productId, state.getValue());
    }

}
