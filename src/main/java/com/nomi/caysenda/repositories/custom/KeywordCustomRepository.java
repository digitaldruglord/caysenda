package com.nomi.caysenda.repositories.custom;

import java.util.List;
import java.util.Set;

public interface KeywordCustomRepository {
    List<String> searchAllByKey(String key, String hostID);
}
