package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.AdminOrderDetailDTO;
import com.nomi.caysenda.dto.PrintOrderDTO;
import com.nomi.caysenda.dto.PrintOrderDetailDTO;
import com.nomi.caysenda.entity.OrderDetailtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailtRepository extends JpaRepository<OrderDetailtEntity,Integer> {
	@Query(	value = "SELECT  odt.id                          AS id," +
			"                odt.quantity                    AS quantity," +
			"                odt.owe                         AS owe," +
			"                odt.price                       AS price," +
			"                odt.pricecn                     AS priceCN," +
			"                odt.cost                        AS cost," +
			"                odt.name                        AS name," +
			"                odt.variant_name                AS variantName," +
			"                odt.product_thumbnail           AS productThumbnail," +
			"                odt.variant_thumbnail           AS variantThumbnail," +
			"                odt.product_id                  AS productId," +
			"                odt.variant_id                  AS variantId," +
			"                odt.weight                      AS weight," +
			"                odt.slug                        AS slug," +
			"                odt.link_zh                     AS linkZh," +
			"                odt.sku                         AS sku," +
			"                odt.name_zh                     AS nameZh," +
			"                odt.variant_name_zh             AS variantNameZh," +
			"                odt.group_name                  AS groupName," +
			"                odt.group_zh_name               AS groupZhName," +
			"                odt.group_sku                   AS groupSku," +
			"                odt.category_id                 AS categoryId," +
			"                odt.unit                        AS unit," +
			"                COALESCE(p.top_flag, '0')       AS topFlag" +
			"        FROM    order_detailt odt" +
			"                LEFT JOIN products p" +
			"                    ON p.id     = odt.product_id" +
			"        WHERE   odt.order_id    = :orderId", nativeQuery = true)
	List<AdminOrderDetailDTO> findDetailtForAdmin(@Param("orderId") Integer orderId);
	List<OrderDetailtEntity> findAllByOrderEntity_Id(Integer orderId);
	@Query(	value = "	SELECT	O.id                        AS id," +
			"					O.quantity                  AS quantity," +
			"					O.owe                       AS owe," +
			"					O.price                     AS price," +
			"					O.priceCN                   AS priceCN," +
			"					O.cost                      AS cost," +
			"					O.name                      AS name," +
			"					O.variant_Name              AS variantName," +
			"					O.product_Thumbnail         AS productThumbnail," +
			"					O.variant_Thumbnail         AS variantThumbnail," +
			"					O.product_Id                AS productId," +
			"					O.variant_Id                AS variantId," +
			"					O.weight                    AS weight," +
			"					O.slug                      AS slug," +
			"					O.link_Zh                   AS linkZh," +
			"					O.sku                       AS sku," +
			"					O.name_Zh                   AS nameZh," +
			"					O.variant_Name_Zh           AS variantNameZh," +
			"					O.group_Name                AS groupName," +
			"					O.group_Zh_Name             AS groupZhName," +
			"					O.group_Sku                 AS groupSku," +
			"					O.category_Id               AS categoryId," +
			"					O.unit                      AS unit," +
			"					COALESCE(P.top_Flag, '0')   AS topFlag" +
			"			FROM	order_detailt O" +
			"					LEFT JOIN products P" +
			"						ON	P.id		= O.product_Id" +
			"			WHERE 	O.order_id	= :i_Id", nativeQuery = true)
	List<PrintOrderDetailDTO> findAllByOrderId(@Param("i_Id") Integer orderId);
}
