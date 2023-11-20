package com.example.appbandienthoai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.appbandienthoai.Class.Account;

import java.time.LocalDate;
import java.util.Calendar;

public class Database extends SQLiteOpenHelper {
    Context context;
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public Database(Context context) {
        super(context, "DBDienThoai.sqlite", null, 1);
    }

    //Truy vấn không trả kết quả
    //Truy vấn không trả kết quả là truy vấn thêm, xóa, sửa trên database
    public void WriteQuery(String content){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(content);
    }
    //Truy vấn trả kết quả (Select)
    public Cursor GetData(String content){
        SQLiteDatabase db =  getReadableDatabase();
        return db.rawQuery(content, null);
    }
    //Hàm AddRole, Thêm dữ liệu vào bảng "ROLE"

    @Override
    public void onCreate(SQLiteDatabase db) {
        //region Tạo bảng ROLE: Quyền hạn
        db.execSQL("CREATE TABLE IF NOT EXISTS [ROLE] (" +
                "QUYENHAN VARCHAR PRIMARY KEY NOT NULL," +
                "NOIDUNG Text NOT NULL)");
        //Thêm dữ liệu vào bảng [ROLE]

        String s = "Insert into [ROLE] values " +
                "('admin', 'Quản trị viên');";

        db.execSQL("Insert into [ROLE] values" +
                "('admin', 'Quản trị viên')," +
                "('customer', 'Khách hàng')");
        // endregion

        //region Tạo bảng ACCOUNT: chứa các tài khoản
        db.execSQL("CREATE TABLE IF NOT EXISTS ACCOUNT (\n" +
                "\tTAIKHOAN VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\tMATKHAU VARCHAR NOT NULL,\n" +
                "\tQUYENHAN VARCHAR NOT NULL, \n" +
                "\tTEN VARCHAR,\n" +
                "\tSDT VARCHAR,\n" +
                "\tGMAIL VARCHAR,\n" +
                "\tDIACHI VARCHAR,\n" +
                "\tFOREIGN KEY (QUYENHAN) REFERENCES [ROLE](QUYENHAN)\n" +
                ");");
        //Thêm tài khoản admin và khách hàng mẫu để test
        db.execSQL("Insert into ACCOUNT values " +
                "('123', '123', 'admin', 'Nguyen Van A', '0924939352', 'voquinamit1@gmail.com', 'thailan'), " +
                "('1234', '1234', 'customer', 'Nguyen Thi B', '0334379439', '', '119');");
        //endregion

        //region Tạo bảng CATEGORY: Phân loại sản phẩm
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS [CATEGORY] (" +
                        "NAME VARCHAR PRIMARY KEY NOT NULL, " +
                        "NOIDUNG VARCHAR);"
        );
        //Thêm một số CATEGORY
        db.execSQL("Insert into [CATEGORY] values " +
                "('IOS', 'IPHONE'), " +
                "('ANDROID', 'SAMSUNG'), " +
                "('CAC LOAI KHAC', 'CAC LOAI KHAC')");
        //endregion

        //region Tạo bảng SẢN PHẨM: Lưu trữ sản phẩm (hoa)
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS SANPHAM (\n" +
                        "MASP VARCHAR PRIMARY KEY NOT NULL,\n" +
                        "TENSP VARCHAR NOT NULL, \n" +
                        "PHANLOAI VARCHAR NOT NULL, \n" +
                        "SOLUONG INTEGER NOT NULL,\n" +
                        "NOINHAP VARCHAR NOT NULL,\n" +
                        "NOIDUNG VARCHAR, \n" +
                        "DONGIA REAL CHECK(DONGIA > 0) NOT NULL,\n" +
                        "HINHANH STRING NOT NULL,\n" +
                        "FOREIGN KEY (PHANLOAI) REFERENCES [CATEGORY](NAME)" +
                        ");"
        );
//        db.execSQL("Insert into SANPHAM values \n" +
//                "('IP001', 'iPhone 15 Pro Max 256GB | Chính hãng VN/A', 'IOS', 5, 'iPhone 15 Pro Max thiết kế mới với chất liệu titan chuẩn hàng không vũ trụ bền bỉ, trọng lượng nhẹ, đồng thời trang bị nút Action và cổng sạc USB-C tiêu chuẩn giúp nâng cao tốc độ sạc. Khả năng chụp ảnh đỉnh cao của iPhone 15 bản Pro Max đến từ camera chính 48MP, camera UltraWide 12MP và camera telephoto có khả năng zoom quang học đến 5x. Bên cạnh đó, iPhone 15 Pro Max sử dụng chip A17 Pro mới mạnh mẽ.', 'Kích thước: 159.9 x 76.7 x 8.25 mm\nKết cấu: Vỏ làm bằng titan, Mặt sau kính mờ, Nút Action\nTrọng lượng: 221 gam\nMàu sắc: Titan đen, Titan trắng, Titan xanh, Titan tự nhiên\nMàn hình: OLED Super Retina 6.7 inch, Thiết kế viên thuốc, Dynamic Island, Màn hình luôn bật\nĐộ phân giải: 2796x1290 pixel\nMật độ điểm ảnh: 460PPI\n Độ sáng tối đa\nTần số quét:  đến 120Hz\nBộ vi xử lý: A17 Pro\nBộ nhớ trong:  256GB, 512GB, 1TB\nThời lượng pin: Lên đến 29 giờ xem video\nCổng sạc : USB-C\nCamera: 48MP + 12MP + 12MP, Camera tele zoom 5x\nHỗ trợ 5G: Có', 34990000, "+R.drawable.ip14prm+"), \n " +
//                "('LK007', 'ASUS ROG Phone 7 Ultimate 16GB 512GB', 'CAC LOAI KHAC', 10, 'Asus ROG phone 7 Ultimate 16GB 512GB sở hữu con chip Snapdragon 8 Gen 2 với sức mạnh siêu khủng đến từ nhà Qualcomm. Màn hình được làm từ màn amoled có kích thước khủng tận 6.78 inch cho chất lượng hình ảnh Full HD Plus. Camera siêu xịn với độ phân giải lên đến 50MP đi kèm viên pin dung lượng vô đối 6000mAh và chế độ sạc HyperCharge 65W.', 'Màn hình: Màn hình chính Samsung AMOLED 6.78 inch, Màn hình ma trận OLED 2 inch ở mặt lưng\nCông nghệ màn hình: Samsung AMOLED\nĐộ phân giải: 2448 X 1080 px\nTần số quét: 165Hz\nChip xử lý: Qualcomm® Snapdragon™ 8 Gen 2\nBộ xử lý đồ hoạ: Adreno 740\nDung lượng RAM: LPDDR5X 16GB\nBộ nhớ trong: 512GB\nDung lượng pin: 6000mAh, Sạc nhanh 65W\nHỗ trợ 5G: Có\nCamera sau: 50MP+13MP+5MP\nCamera trước: 32MP\nCổng sạc: Type-C\nTrọng lượng: 246g\nMàu sắc: Trắng', 29990000, "+R.drawable.ip14prm+"),   \n"+
//                "('AD002', 'Samsung Galaxy S23 Ultra 256GB', 'ANDROID', 10, 'Samsung S23 Ultra là dòng điện thoại cao cấp của Samsung, sở hữu camera độ phân giải 200MP ấn tượng, chip Snapdragon 8 Gen 2 mạnh mẽ, bộ nhớ RAM 8GB mang lại hiệu suất xử lý vượt trội cùng khung viền vuông vức sang trọng. Sản phẩm được ra mắt từ đầu năm 2023.', 'Kích thước màn hình: 6.8 inch\nMàu sắc: Xanh Botanic, Đen Phantom, Tím Lilac, Kem Cotton\nMàu sắc đặc quyền: Xanh Lime, Xanh SkyBlue, Xám Graphite, Đỏ Lychee\nCông nghệ màn hình: Dynamic AMOLED 2X\nĐộ phân giải; 3088 x 1440 (Edge Quad HD+)\nTần số màn hình: Tần số quét tương thích 1~120Hz\nCamera trước: 12MP\nBộ vi xử lý (CPU): Snapdragon 8 Gen 2: Bộ vi xử lý đồ hoạ (GPU): Adreno 740\nRAM: 8GB\nBộ nhớ trong: 256GB/512GB/1TB\nPin: 5000mAh\nCổng sạc: USB Type-C\nSố lượng thẻ Sim: 2 SIM (Nano SIM)\nCông nghệ NFC\nHỗ trợ mạng: 5G\nKích thước: \t163.4 x 78.1 x 8.9 mm\nChỉ số kháng nước, bụi: ', 22890000, "+R.drawable.ip14prm+"), \n" +
//                "('LK003', 'Xiaomi Redmi Note 12 8GB 128GB', 'CAC LOAI KHAC', 10, 'Xiaomi Redmi Note 12 8GB 128GB tỏa sáng với diện mạo viền vuông cực thời thượng cùng hiệu suất mạnh mẽ nhờ sở hữu con chip Snapdragon 685 ấn tượng. Chất lượng hiển thị hình ảnh của Redmi Note 12 Vàng cũng khá sắc nét thông qua tấm nền AMOLED 120Hz hiện đại. Chưa hết, máy còn sở hữu cụm 3 camera với độ rõ nét lên tới 50MP cùng viên pin 5000mAh và s ạc nhanh 33W giúp đáp ứng được mọi nhu cầu sử dụng của người dùng.','Màn hình: Màn đục lỗ 6.67 inch, độ phân giải full HD+, sử dụng tấm nền AMOLED hỗ trợ Dolby Atmos và Dolby Vision.\nRAM: 4GB/8GB.\nROM: 128GB.\nVi xử lý: Snapdragon 685.\nPin: 5.000 mAh hỗ trợ sạc nhanh 33W.\nCamera sau: 50MP + 8MP + 2MP.\nCamera trước: 13MP.', 4990000, "+R.drawable.ip14prm+"), \n" +
//                "('IP004', 'iPhone 14 Pro Max 128GB | Chính hãng VN/A', 'IOS', 4, 'iPhone 14 Pro Max sở hữu thiết kế màn hình Dynamic Island ấn tượng cùng màn hình OLED 6,7 inch hỗ trợ always-on display và hiệu năng vượt trội với chip A16 Bionic. Bên cạnh đó máy còn sở hữu nhiều nâng cấp về camera với cụm camera sau 48MP, camera trước 12MP dùng bộ nhớ RAM 6GB đa nhiệm vượt trội.Kích thước: 160.7 x 77.6 x 7.9 mm\nTrọng lượng: 240 gamMàu sắc: Đen, Tím, Bạc, Vàng\nMàn hình: OLED Retina 6.7 inch, Thiết kế viên thuốc, Dynamic Island, Màn hình luôn bật\nĐộ phân giải: 2796 x 1290 pixel\nMật độ điểm ảnh: 460 PPI\nĐộ sáng tối đa: 2000 nits\nTần số quét: Từ 1Hz đến 120Hz\nBộ vi xử lý: A16 Bionic\nDung lượng RAM: 6GB\nBộ nhớ trong: 128GB, 256GB, 512GB, 1TB\nDung lượng pin: 4352 mAh\nCông suất sạc : 20W khi sạc có dây,  15W khi sạc Magsafe không dây, 7,5W khi sạc Qi không dây\nCamera: 48MP + 12MP + 12MP\nHỗ trợ 5G: Có', '', 26090000, "+R.drawable.ip14prm+"),  \n" +
//                "('LK005', 'Xiaomi 13T 12GB 256GB', 'CAC LOAI KHAC', 3, 'Xiaomi 13T đem tới trải nghiệm siêu mượt mà cho người dùng khi được trang bị chipset mạnh mẽ MediaTek Dimensity 8200-Ultra. Màn hình AMOLED thế hệ mới với tần số quét 144Hz giúp chất lượng hiển thị được sắc nét và chân thực trong từng điểm ảnh. Hệ thống quay chụp của máy cũng cực kỳ ấn tượng với cảm biến camera lên tới 50MP. Đồng thời, viên pin lên tới 5000mAh kết hợp với sạc nhanh 67W giúp nâng cao thời lượng sử dụng của người dùng.Màu sắc: Xanh tuyết sớn - Xanh thảo nguyên - Đen\nTrọng lượng: 193g - 197g\nMàn hình: AMOLED Flat display 6.67 inch, Tần số quét 144Hz, Độ sáng tối đa 2600 nits\nCPU: MediaTek Dimensity 8200-Ultra, Tiến trình 4nm\nGPU: Mali-G610\nRAM:8GB - 12GB\nROM: 256GB\nCamera trước: Camera selfie 20MP\nCamera sau: Camera chính Leica 50MP f/1.9, Camera góc siêu rộng 12MP f/2.2, Camera tele 50MP f/1.9\nDung lượng pin: Pin 5000mAh - Sạc nhanh 67W\nTản nhiệt: Công nghệ LiquidCool, (Thép không gỉ VC 5000mm2,Tấm than chì nhiều lớp)', '', 12990000, "+R.drawable.ip14prm+"),  \n" +
//                "('AD001', 'Samsung Galaxy Z Fold5 12GB 256GB', 'ANDROID', 7, 'Samsung Galaxy Z Fold5 12GB 256GB tạo nên trải nghiệm xử lý tác vụ siêu mượt mà thông qua chipset Snapdragon 8 Gen 2 đỉnh cao cùng dung lượng RAM lên tới 12GB. Máy được trang bị công nghệ màn hình Dynamic AMOLED 2X 120Hz với kích thước có thể lên tới 7.6 inch khi mở, đem lại trải nghiệm hình ảnh sắc nét trong từng điểm ảnh. Bên cạnh đó, phân khúc smartphone gập này còn sở hữu cụm camera hiện đại với độ sắc nét đạt tới 50MP cùng viên pin 4400mAh.', 'Màn hình: 6.7 inch, Dynamic AMOLED, FHD+, 1080 x 2636 Pixels\nCamera Selfie: \t10.0 MP\nRAM: 8GB\nBộ nhớ trong: 256 GB\nCPU: Snapdragon 8 Gen 2', 33990000, "+R.drawable.ip14prm+"),  \n" +
//                "('IP002', 'iPhone 15 128GB | Chính hãng VN/A', 'IOS', 2, 'iPhone 15 128GB được trang bị màn hình Dynamic Island kích thước 6.1 inch với công nghệ hiển thị Super Retina XDR màn lại trải nghiệm hình ảnh vượt trội. Điện thoại với mặt lưng kính nhám chống bám mồ hôi cùng 5 phiên bản màu sắc lựa chọn: Hồng, Vàng, Xanh lá, Xanh dương và đen. Camera trên iPhone 15 series cũng được nâng cấp lên cảm biến 48MP cùng tính năng chụp zoom quang học tới 2x. Cùng với thiết kế cổng sạc thay đổi từ lightning sang USB-C vô cùng ấn tượng.', 'Kích thước màn hình: 6.1 inch\nTấm nền màn hình: OLED\nCông nghệ màn hình: Dynamic Island,Super Retina XDR\nMàu sắc: Hồng, Vàng, Xanh lá, Xanh Dương, Đen\nChất liệu: Mặt trước Ceramic Shield, Khung viền nhôm\nCPU: Chip A16 Bionic - CPU 6 lõi\nCPU: GPU 5 lõi\nBộ nhớ lưu trữ: 128GB - 256GB - 512GB\nCamera sau: Camera chính 48MP, Camera Ultra Wide 12MP\nCamera trước: Camera trước 12MP\nTính năng quay – chụp: Ảnh chân dung thế hệ mới, Focus và Depth Control\nLựa chọn thu phóng: 0.5x - 1x - 2x\nPin: Thời gian xem video lên đến 20 giờ\nCổng sạc: USB-C\nTính năng nâng cao: SOS Khẩn Cấp, Phát Hiện Va Chạm\nBảo mật: Face ID', 22490000, "+R.drawable.ip14prm+"),  \n" +
//                "('AD003', 'Samsung Galaxy Z Flip5 256GB', 'ANDROID', 4, 'Samsung Galaxy Z Flip 5 có thiết kế màn hình rộng 6.7 inch và độ phân giải Full HD+ (1080 x 2640 Pixels), dung lượng RAM 8GB, bộ nhớ trong 256GB. Màn hình Dynamic AMOLED 2X của điện thoại này hiển thị rõ nét và sắc nét, mang đến trải nghiệm ấn tượng khi sử dụng.', 'Màn hình: 6.7 inch, Dynamic AMOLED, FHD+, 1080 x 2636 Pixels\nCamera Selfie: 10.0 MP\nRAM: 8GB\nBộ nhớ trong: 256GB\nCPU: Snapdragon 8 Gen 2', 19790000, "+R.drawable.ip14prm+"),  \n" +
//                "('LK004', 'Nubia Neo 8GB 256GB', 'CAC LOAI KHAC', 1, 'Nubia Neo, mẫu điện thoại gaming rẻ giá rẻ sở hữu cấu hình khủng với chipset T820 2.7GHz của Unisoc, màn hình 6,6 inch 120Hz và tổng 16GB dung lượng RAM. Đi kèm cấu hình khủng, đáp ứng tốt nhu cầu chơi game của điện thoại Nubia là nhiều tính năng nổi bật như hệ thống tản nhiệt nhiều lớp, mở khóa khuôn mặt, kết nối 5G, NFC,...', 'Màn hình: ISP LCD, 120Hz 6.6 inches, Full HD+ (1080 x 2408 pixels)\nCamera trước: 8 MP\nCPU: Unisoc T820 (6 nm) 8 nhân (1x2.7 GHz & 3x2.3 GHz & 4x2.1 GHz) GPU: Mali-G57\nRAM: 8GB (+10GB RAM ảo), LPDDR4X\nBộ nhớ trong: 256GB, UFS 2.2', 4690000, "+R.drawable.ip14prm+"),  \n" +
//                "('IP005', 'iPhone 13 Pro Max 128GB | Chính hãng VN/A', 'IOS', 0, 'iPhone 13 Pro Max chắc chắn sẽ là chiếc smartphone cao cấp được quan tâm nhiều nhất trong năm 2021. Dòng iPhone 13 series được ra mắt vào ngày 14 tháng 9 năm 2021 tại sự kiện \"California Streaming\" diễn ra trực tuyến tương tự năm ngoái cùng với 3 phiên bản khác là iPhone 13, 13 mini và 13 Pro.', 'Màu sắc: Bạc, Xanh lá, Vàng, Xám, Xanh Dương\nTrọng lượng: 240g\nMàn hình: 6.7 inches Super Retina XDR OLED\nCPU: Apple A15 3.22 GHz\nGPU: GPU 5 nhân\nRAM: 6 GB\nROM: 128 GB\nCamera trước: 12MP, ƒ/2.2\nCamera sau: Camera góc rộng: 12MP, ƒ/1.5, Camera góc siêu rộng: 12MP, ƒ/1.8, Camera tele : 12MP, /2.8\nDung lượng pin: 4,352mAh, Sạc nhanh 20W', 24290000, "+R.drawable.ip14prm+"),  \n" +
//                "('IP006', 'iPhone 12 Pro Max 128GB I Chính hãng VN/A', 'IOS', 3, 'Cứ mỗi năm, đến dạo cuối tháng 8 và gần đầu tháng 9 thì mọi thông tin sôi sục mới về chiếc iPhone mới lại xuất hiện. Apple năm nay lại ra thêm một chiếc iPhone mới với tên gọi mới là iPhone 12 Pro Max, đây là một dòng điện thoại mới và mạnh mẽ nhất của nhà Apple năm nay.', 'Màn hình: 6.7 inch, OLED, Super Retina XDR, 2778 x 1284 Pixels\nCamera sau: 12.0 MP + 12.0 MP + 12.0 MP\nCamera Selfie: 12.0 MP\nRAM: 6 GB\nBộ nhớ trong: 128 GB', 23490000, "+R.drawable.ip14prm+");");

        // Region Tạo bảng BILL: Lưu trữ các hóa đơn của người mua
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS BILL (\n" +
                        "   ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "   DATEORDER date NOT NULL,\n" +
                        "   TAIKHOANCUS VARCHAR NOT NULL,\n" +
                        "   NAMECUS VARCHAR NOT NULL,\n" +
                        "   ADDRESSDELIVERRY VARCHAR NOT NULL,\n" +
                        "   SDT VARCHAR not null);"
        );
        //endregion

        //region Tạo bảng Bill_Detail: Chi tiết hóa đơn
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS BILLDETAIL (\n" +
                        "    ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "    MASP VARCHAR NOT NULL,\n" +
                        "    IDORDER   INTEGER not NULL,\n" +
                        "    IDVoucher VARCHAR not null, \n" +
                        "    QUANTITY  INTEGER check(QUANTITY > 0) not NULL,\n" +
                        "    UNITPRICE Real check(UNITPRICE > 0) not NULL,\n" +
                        "    TOTALPRICE Real check (TOTALPRICE > 0) not Null,\n" +
                        "    FOREIGN KEY (MASP) REFERENCES SANPHAM(MASP),\n" +
                        "    FOREIGN KEY (IDORDER) REFERENCES BILL(ID), \n" +
                        "    FOREIGN KEY (IDVoucher) REFERENCES VOUCHER(MAVOUCHER)" +
                        ");"
        );

        //endregion

        //region Tạo bảng VOUCHER: Lưu trữ các voucher hiện có
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS VOUCHER(\n" +
                        "\tMAVOUCHER VARCHAR PRIMARY KEY not null,\n" +
                        "\tNOIDUNG TEXT," +
                        "\tHSD date," +
                        "\tGIAM INTEGER DEFAULT(1) Check(GIAM >= 0)\n" +
                        ");"
        );
        int year = LocalDate.now().getYear();
        db.execSQL("Insert into VOUCHER values \n" +
                "('SALET5', 'Sale tháng 5', '2023-05-31' , 10.0/100), \n" +
                "('SALENEW', 'Sale mới', '2023-05-31' , 30.0/100), \n" +
                "('TVBTRAN', 'Sale báo', '2023-02-31' , 15.0/100), \n" +
                "('TVBTRAN19T2', 'Vẫn là báo sale', '2023-04-31' , 20.0/100)");

        //endregion

        //region Tạo bảng VOUCHER DETAIL: Chi tiết voucher sử dụng cho một hoặc nhiều sản phẩm cụ thể
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS VOUCHER_DETAIL(\n" +
                        "MAVOUCHER VARCHAR," +
                        "MASP VARCHAR NOT NULL," +
                        "FOREIGN KEY (MAVOUCHER) REFERENCES VOUCHER(MAVOUCHER)," +
                        "  FOREIGN KEY (MASP) REFERENCES SANPHAM(MASP)" +
                        ");"
        );
        db.execSQL("Insert into VOUCHER_DETAIL values " +
                "('SALET5', 'CB001'), " +
                "('SALET5', 'CB002'), " +
                "('SALET5', 'CB003') ");
        //endregion
        //region Tạo bảng CARTLIST: Lưu trữ giỏ hàng của người dùng, tự động cập nhật khi người dùng đăng nhập lại
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS CARTLIST (\n" +
                        "\tIDCARTLIST   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "\tIDCUS        VARCHAR,\n" +
                        "\tIDSANPHAM    VARCHAR NOT NULL,\n" +
                        "\tIDVoucher    VARCHAR,\n" +
                        "\tSOLUONG      INTEGER CHECK(SOLUONG > 0) NOT NULL," +
                        "\tDONGIA       REAL,\n" +
                        "\tFOREIGN KEY (IDCUS) REFERENCES ACCOUNT(TAIKHOAN),\n" +
                        "\tFOREIGN KEY (IDSANPHAM) REFERENCES SANPHAM(MASP), \n" +
                        "\tFOREIGN KEY (IDVoucher) REFERENCES VOUCHER(MAVOUCHER)\n" +
                        ")"
        );
        //endregion
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //Thêm nội dung vào db
    private boolean CheckExists(String PrimaryColumn, String TableName){
        Cursor i = this.GetData("Select* from " + TableName);
        while (i.moveToNext()){
            if (PrimaryColumn.equals(i.getString(0))){
                return false;
            }
        }
        return true;
    }
    public boolean AddAccount(String taikhoan, String matkhau, String quyenhan, String ten, String sdt, String gmail,String diachi){
        boolean check = CheckExists(taikhoan, "Account");
        if (check){
            this.WriteQuery("Insert into ACCOUNT Values" +
                    "('" + taikhoan + "', '" + matkhau + "', '" + quyenhan + "', '" + ten + "', '" + sdt + "', '" + gmail + "','" + diachi + "');");
        }
        return check;
    }
    public boolean AddRole(String role, String content){
        boolean check = CheckExists(role, "[ROLE]");
        if (check){
            this.WriteQuery("Insert into [ROLE] Values" +
                    "('" + role + "', '" + content + "');");
        }
        return check;
    }
    public boolean AddProduct(String MASP, String TENSP, String PHANLOAI, Integer SOLUONG, String NOINHAP, String NOIDUNG, double DONGIA, String HINHANH){
        boolean check = CheckExists(MASP, "SANPHAM");
        if (check){
            LocalDate currentDate = LocalDate.now(); //định dạng ngày sẽ là "YYYY/MM/dd"
            this.WriteQuery("Insert into SANPHAM Values" +
                    "('" + MASP + "', '" + TENSP + "', '" + PHANLOAI + "', '" + SOLUONG + "', '" + NOINHAP + "', '" + NOIDUNG + "', '" + DONGIA + "', '" + HINHANH + "'" + ");");
        }
        return check;
    }
    public boolean AddCategory(String NAME, String CONTENT){
        boolean check = CheckExists(NAME, "[CATEGORY]");
        if (check){
            this.WriteQuery("Insert into [CATEGORY] Values" +
                    "('" + NAME + "', '" + CONTENT + "');");
        }
        return check;
    }
    public boolean AddBill(String TAIKHOANCUS, String ADDRESSDELIVERRY) {
        try {
            Calendar c = Calendar.getInstance();
            String DATEORDER = Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(c.get(Calendar.MONTH) + 1) + "/" + Integer.toString(c.get(Calendar.YEAR));
            SQLiteDatabase db = getReadableDatabase();
            this.WriteQuery("Insert into [CATEGORY] (DATEORDER, TAIKHOANCUS, ADDRESSDELIVERRY) Values" +
                    "('" + DATEORDER + "', '" + TAIKHOANCUS + "', '" + ADDRESSDELIVERRY + "');");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean AddVoucher(String MAVOUCHER, String NOIDUNG, String HSD, double GIAM){
        boolean check = CheckExists(MAVOUCHER, "VOUCHER");
        if (check){
            this.WriteQuery("Insert into VOUCHER values" +
                    "('"+ MAVOUCHER +"', '"+ NOIDUNG +"', '"+ HSD +"', "+ GIAM +");");
        }
        return check;
    }
    public boolean AddVoucherProduct(String MAVOUCHER, String MASP){
        try{
            Cursor cursor = this.GetData(
                    "Select*" +
                            " From VOUCHER_DETAIL" +
                            " Where MAVOUCHER = '" + MAVOUCHER + "'" +
                            " and MASP = '" + MASP + "'"
            );
            if (!cursor.moveToFirst()){
                this.WriteQuery("Insert into VOUCHER_DETAIL values" +
                        "('" + MAVOUCHER + "', '" + MASP + "')");
                return true;
            }
            return false;
        }
        catch (Exception e){
            return false;
        }
    }
    public long addCartList(GioHang gioHang) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IDCUS", gioHang.getIdCus());
        values.put("IDSANPHAM", gioHang.getIdSanPham());
        values.put("IDVoucher", gioHang.getIdVoucher());
        values.put("SOLUONG", gioHang.getSoLuong());
        values.put("DONGIA", gioHang.getDonGia());
        return db.insert("CARTLIST", "IDCUS", values);
    }
    public long updateCartList(GioHang gioHang) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IDCARTLIST", gioHang.getIdCartList());
        values.put("IDCUS", gioHang.getIdCus());
        values.put("IDSANPHAM", gioHang.getIdSanPham());
        values.put("IDVoucher", gioHang.getIdVoucher());
        values.put("SOLUONG", gioHang.getSoLuong());
        values.put("DONGIA", gioHang.getDonGia());

        long kq = db.update("CARTLIST", values, "IDCARTLIST=?",  new String[]{gioHang.getIdCartList().toString()});
        db.close();
        if(kq <= 0){
            return -1;//Thêm thất bại
        }
        return 1;//Thêm thành công
    }

    public void updateUser(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//            values.put("TAIKHOAN",account.getTAIKHOAN());
//            values.put("MATKHAU",account.getMATKHAU());
//            values.put("QUYENHAN",account.getQUYENHAN());
        values.put("TEN", account.getTEN());
        values.put("SDT", account.getSDT());
        values.put("GMAIL", account.getGMAIL());
        values.put("DIACHI",account.getDIACHI());

        long check = db.update("ACCOUNT",values,"TAIKHOAN=?",new String[]{account.getTAIKHOAN().toString()});
        //long check = db.insert("ACCOUNT",null,values);
//            if (check != -1){
//                Toast.makeText(context,"Saved", Toast.LENGTH_SHORT).show();
//                db.close();
//
//            }else {
//                Toast.makeText(context, "Chỉnh sửa thất bại", Toast.LENGTH_SHORT).show();
//            }
    }

    public Cursor getUser(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from ACCOUNT",null);
        return cursor;
    }

    public long changePassword(String user, String oldPass, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MATKHAU", newPass);
        return db.update("ACCOUNT", values, "TAIKHOAN=?", new String[]{user});
    }
}
