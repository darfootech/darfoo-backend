package com.darfoo.backend.service.cota;

import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.service.responsemodel.SingleVideo;

import java.util.HashMap;

/**
 * Created by zjh on 15-2-24.
 */
public class TypeClassMapping {
    public static HashMap<String, Class> typeClassMap = new HashMap<String, Class>();
    public static HashMap<String, Class> cacheResponseMap = new HashMap<String, Class>();

    static {
        typeClassMap.put("video", Video.class);
        typeClassMap.put("tutorial", Tutorial.class);
        typeClassMap.put("music", Music.class);
        typeClassMap.put("author", Author.class);
        typeClassMap.put("uploadnoauthvideo", UploadNoAuthVideo.class);

        cacheResponseMap.put("video", SingleVideo.class);
        cacheResponseMap.put("tutorial", SingleVideo.class);
        cacheResponseMap.put("music", SingleMusic.class);
        cacheResponseMap.put("author", SingleAuthor.class);
    }
}
