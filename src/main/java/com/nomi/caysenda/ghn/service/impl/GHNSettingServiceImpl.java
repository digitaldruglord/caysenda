package com.nomi.caysenda.ghn.service.impl;
import com.nomi.caysenda.ghn.entity.GHNSettingEntity;
import com.nomi.caysenda.ghn.repositories.GhnSettingRepository;
import com.nomi.caysenda.ghn.service.GHNSettingService;
import com.nomi.caysenda.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class GHNSettingServiceImpl implements GHNSettingService {
    @Autowired
    GhnSettingRepository settingRepository;
    @Override
    public <T> T getData(String optionKey, Class<T> tClass) {
        GHNSettingEntity entity = settingRepository.findByKeySetting(optionKey);
        if (entity == null) return null;
        return SpringUtils.convertJsonToObject(entity.getValueSetting(), tClass);
    }

    @Override
    public <T> void update(T t, String optionKey) {
        GHNSettingEntity entity = settingRepository.findByKeySetting(optionKey);
        if (entity == null) {
            entity = new GHNSettingEntity();
            entity.setKeySetting(optionKey);
            entity.setValueSetting(SpringUtils.convertObjectToJson(t));


        } else {
            entity.setValueSetting(SpringUtils.convertObjectToJson(t));

        }
        settingRepository.save(entity);
    }

    @Override
    public void delete(String optionKey) {
        settingRepository.deleteByKeySetting(optionKey);
    }
}
