package com.nomi.caysenda.dto;

public class KeywordStatictisDTO {
    String keyword;
    Long count;

    public KeywordStatictisDTO(String keyword, Long count) {
        this.keyword = keyword;
        this.count = count;
    }

    public KeywordStatictisDTO() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
