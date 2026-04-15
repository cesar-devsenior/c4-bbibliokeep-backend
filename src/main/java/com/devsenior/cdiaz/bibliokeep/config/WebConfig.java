package com.devsenior.cdiaz.bibliokeep.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.file.directory}")
    private String directoryName;

    @Value("${app.file.publish-path}")
    private String publishPathName;

    @Value("${app.file.cache-period}")
    private Integer cachePeriod;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        var uploadPath = Paths.get(directoryName).toFile().getAbsolutePath();

        // Quiero registrar que las peticiones al endpoint '/public/**' se muestrelos archivos del directorio 'file:./files/upload/'
        registry.addResourceHandler("%s/**".formatted(publishPathName))
                .addResourceLocations("file:%s/".formatted(uploadPath))
                .setCachePeriod(cachePeriod);
    }
}
