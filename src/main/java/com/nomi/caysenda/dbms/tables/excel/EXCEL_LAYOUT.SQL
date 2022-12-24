DROP TABLE EXCEL_LAYOUTDEF;
CREATE TABLE IF NOT EXISTS EXCEL_LAYOUTDEF(
    EXCEL_CD        VARCHAR(256),
    COLUMN_CD       VARCHAR(256),
    COLUMN_NAME     VARCHAR(256),
    COLUMN_TYPE     VARCHAR(256),
    ADDDATETIME     TIMESTAMP DEFAULT (CURRENT_DATE),
    UDPDATETIME     TIMESTAMP DEFAULT (CURRENT_DATE)
);

ALTER TABLE EXCEL_LAYOUTDEF ADD PRIMARY KEY (EXCEL_CD, COLUMN_CD);
ALTER TABLE EXCEL_LAYOUTDEF ADD INDEX (EXCEL_CD);

#LAYOUT SAPO
INSERT INTO EXCEL_LAYOUTDEF(EXCEL_CD, COLUMN_CD, COLUMN_NAME, COLUMN_TYPE)
VALUES  ('EXPORT_SAPO', 'SLUG',                             'Đường dẫn / Alias',                                                'STRING'),
        ('EXPORT_SAPO', 'PRODUCT_NAME',                     'Tên sản phẩm',                                                     'STRING'),
        ('EXPORT_SAPO', 'CONTENT',                          'Nội dung',                                                         'STRING'),
        ('EXPORT_SAPO', 'SUPPLIER',                         'Nhà cung cấp',                                                     'STRING'),
        ('EXPORT_SAPO', 'CATEGORY_CD',                      'Loại',                                                             'STRING'),
        ('EXPORT_SAPO', 'TAGS',                             'Tags',                                                             'STRING'),
        ('EXPORT_SAPO', 'DISPLAY',                          'Hiển thị',                                                         'STRING'),
        ('EXPORT_SAPO', 'ATTRIBUTE1_NAME',                  'Thuộc tính 1(Option1 Name)',                                       'STRING'),
        ('EXPORT_SAPO', 'ATTRIBUTE1_VALUE',                 'Giá trị thuộc tính 1(Option1 Value)',                              'STRING'),
        ('EXPORT_SAPO', 'ATTRIBUTE2_NAME',                  'Thuộc tính 2(Option2 Name)',                                       'STRING'),
        ('EXPORT_SAPO', 'ATTRIBUTE2_VALUE',                 'Giá trị thuộc tính 2(Option2 Value)',                              'STRING'),
        ('EXPORT_SAPO', 'ATTRIBUTE3_NAME',                  'Thuộc tính 3(Option3 Name)',                                       'STRING'),
        ('EXPORT_SAPO', 'ATTRIBUTE3_VALUE',                 'Giá trị thuộc tính 3(Option3 Value)',                              'STRING'),
        ('EXPORT_SAPO', 'OPERATOR_CD',                      'Quản lý kho',                                                      'STRING'),
        ('EXPORT_SAPO', 'STOCK',                            'Mã (SKU)',                                                         'STRING'),
        ('EXPORT_SAPO', 'DENY',                             'Số lượng',                                                         'STRING'),
        ('EXPORT_SAPO', 'VARIANT_FULFILLMENT_SERVICE',      'Variant Fulfillment Service',                                      'STRING'),
        ('EXPORT_SAPO', 'PRICE',                            'Giá',                                                              'STRING'),
        ('EXPORT_SAPO', 'COMPARTIVE_PRICE',                 'Giá so sánh',                                                      'STRING'),
        ('EXPORT_SAPO', 'REQUIREOFDELIVERY',                'Yêu cầu vận chuyển',                                               'STRING'),
        ('EXPORT_SAPO', 'VAT',                              'VAT',                                                              'STRING'),
        ('EXPORT_SAPO', 'BARCODE',                          'Mã vạch(Barcode)',                                                 'STRING'),
        ('EXPORT_SAPO', 'THUMBNAIL',                        'Ảnh đại diện',                                                     'STRING'),
        ('EXPORT_SAPO', 'THUMBNAIL_NOTE',                   'Chú thích ảnh',                                                    'STRING'),
        ('EXPORT_SAPO', 'SEO_TITLE',                        'Thẻ tiêu đề(SEO Title)',                                           'STRING'),
        ('EXPORT_SAPO', 'SEO_DESCRIPTION',                  'Thẻ mô tả(SEO Description)',                                       'STRING'),
        ('EXPORT_SAPO', 'WEIGTH',                           'Cân nặng',                                                         'STRING'),
        ('EXPORT_SAPO', 'WEIGTH_UNIT',                      'Đơn vị cân nặng',                                                  'STRING'),
        ('EXPORT_SAPO', 'IMAGE_VERSION',                    'Ảnh phiên bản',                                                    'STRING'),
        ('EXPORT_SAPO', 'SHORT_DESCRIPTION',                'Mô tả ngắn',                                                       'STRING');

