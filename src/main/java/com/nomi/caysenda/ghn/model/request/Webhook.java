package com.nomi.caysenda.ghn.model.request;

import com.nomi.caysenda.ghn.model.responses.DataFeeCreateOrderModelResponse;

public class Webhook {
    Long CODAmount;
    String ClientOrderCode;
    Integer ConvertedWeight;
    String Description;
    DataFeeCreateOrderModelResponse fee;
    Integer Height;
    Integer Length;
    String OrderCode;
    String Reason;
    String ReasonCode;
    String ShipperName;
    String ShipperPhone;
    String Status;
    String Time;
    Long TotalFee;
    String Type;
    String Warehouse;
    Integer Weight;
    Integer Width;

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public Long getCODAmount() {
        return CODAmount;
    }

    public void setCODAmount(Long CODAmount) {
        this.CODAmount = CODAmount;
    }

    public String getClientOrderCode() {
        return ClientOrderCode;
    }

    public void setClientOrderCode(String clientOrderCode) {
        ClientOrderCode = clientOrderCode;
    }

    public Integer getConvertedWeight() {
        return ConvertedWeight;
    }

    public void setConvertedWeight(Integer convertedWeight) {
        ConvertedWeight = convertedWeight;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public DataFeeCreateOrderModelResponse getFee() {
        return fee;
    }

    public void setFee(DataFeeCreateOrderModelResponse fee) {
        this.fee = fee;
    }

    public Integer getHeight() {
        return Height;
    }

    public void setHeight(Integer height) {
        Height = height;
    }

    public Integer getLength() {
        return Length;
    }

    public void setLength(Integer length) {
        Length = length;
    }



    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public String getShipperPhone() {
        return ShipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        ShipperPhone = shipperPhone;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Long getTotalFee() {
        return TotalFee;
    }

    public void setTotalFee(Long totalFee) {
        TotalFee = totalFee;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getWarehouse() {
        return Warehouse;
    }

    public void setWarehouse(String warehouse) {
        Warehouse = warehouse;
    }

    public Integer getWeight() {
        return Weight;
    }

    public void setWeight(Integer weight) {
        Weight = weight;
    }

    public Integer getWidth() {
        return Width;
    }

    public void setWidth(Integer width) {
        Width = width;
    }
}
