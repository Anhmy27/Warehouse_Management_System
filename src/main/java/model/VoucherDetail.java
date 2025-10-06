package model;

public class VoucherDetail {
    private String voucherDetailId;
    private String voucherId;
    private String productId;
    private String productName;   // ✅ thêm
    private String productImg;    // ✅ thêm
    private int quantityExpected;
    private int quantityActual;
    private double price;
    private double total;

    public VoucherDetail() {}

    public String getVoucherDetailId() {
        return voucherDetailId;
    }

    public void setVoucherDetailId(String voucherDetailId) {
        this.voucherDetailId = voucherDetailId;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {          // ✅ thêm getter
        return productName;
    }

    public void setProductName(String productName) {   // ✅ sửa lại setter
        this.productName = productName;
    }

    public String getProductImg() {           // ✅ thêm getter
        return productImg;
    }

    public void setProductImg(String productImg) {     // ✅ sửa lại setter
        this.productImg = productImg;
    }

    public int getQuantityExpected() {
        return quantityExpected;
    }

    public void setQuantityExpected(int quantityExpected) {
        this.quantityExpected = quantityExpected;
    }

    public int getQuantityActual() {
        return quantityActual;
    }

    public void setQuantityActual(int quantityActual) {
        this.quantityActual = quantityActual;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
