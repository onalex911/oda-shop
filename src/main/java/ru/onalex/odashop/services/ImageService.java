package ru.onalex.odashop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ImageService {
    public static final String ASSETS = "/assets/";
    public static final String IMAGE_FULL_PATH = "/static" + ASSETS;
    public static final String IMAGE_NOT_FOUND_PATH = ASSETS + "images/image_not_found.png";

    private final ResourceLoader resourceLoader;
    private final Map<String, Boolean> imageExistsCache = new ConcurrentHashMap<>();

    @Autowired
    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getImagePath(String picPath) {
        // Проверяем кеш сначала
        String picFullPath = IMAGE_FULL_PATH + picPath;
        if (picPath.isEmpty())
            return IMAGE_NOT_FOUND_PATH;
        if (imageExistsCache.containsKey(picFullPath)) {
            return imageExistsCache.get(picPath)
                    ? ASSETS + picPath
                    : IMAGE_NOT_FOUND_PATH;
        }
        Resource resource = resourceLoader.getResource("classpath:" + picFullPath);

        boolean exists = resource.exists();
        imageExistsCache.put(picPath, exists);

        return exists ? ASSETS + picPath : IMAGE_NOT_FOUND_PATH;
    }

    // Метод для очистки кеша при необходимости
    public void clearCache() {
        imageExistsCache.clear();
    }
}
