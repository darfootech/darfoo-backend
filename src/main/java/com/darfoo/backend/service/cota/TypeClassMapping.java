package com.darfoo.backend.service.cota;

import com.darfoo.backend.model.auth.Feedback;
import com.darfoo.backend.model.cota.AuthorType;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.model.statistics.CrashLog;
import com.darfoo.backend.model.statistics.SearchHistory;
import com.darfoo.backend.model.statistics.clickcount.MenuClickCount;
import com.darfoo.backend.model.statistics.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.clickcount.TabClickCount;
import com.darfoo.backend.model.statistics.clicktime.MenuClickTime;
import com.darfoo.backend.model.statistics.clicktime.ResourceClickTime;
import com.darfoo.backend.model.statistics.clicktime.TabClickTime;
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
    public static HashMap<String, Class> clickCountStatMap = new HashMap<String, Class>();
    public static HashMap<String, Class> clickTimeStatMap = new HashMap<String, Class>();
    public static HashMap<String, AuthorType> authorTypeMap = new HashMap<String, AuthorType>();

    static {
        typeClassMap.put("video", Video.class);
        typeClassMap.put("tutorial", Tutorial.class);
        typeClassMap.put("music", Music.class);
        typeClassMap.put("author", Author.class);
        typeClassMap.put("uploadnoauthvideo", UploadNoAuthVideo.class);
        typeClassMap.put("resourceclickcount", ResourceClickCount.class);
        typeClassMap.put("menuclickcount", MenuClickCount.class);
        typeClassMap.put("tabclickcount", TabClickCount.class);
        typeClassMap.put("resourceclicktime", ResourceClickTime.class);
        typeClassMap.put("menuclicktime", MenuClickTime.class);
        typeClassMap.put("tabclicktime", TabClickTime.class);
        typeClassMap.put("searchhistory", SearchHistory.class);
        typeClassMap.put("feedback", Feedback.class);
        typeClassMap.put("crashlog", CrashLog.class);

        cacheResponseMap.put("video", SingleVideo.class);
        cacheResponseMap.put("tutorial", SingleVideo.class);
        cacheResponseMap.put("music", SingleMusic.class);
        cacheResponseMap.put("author", SingleAuthor.class);

        clickCountStatMap.put("menu", MenuClickCount.class);
        clickCountStatMap.put("tab", TabClickCount.class);

        clickTimeStatMap.put("menu", MenuClickTime.class);
        clickTimeStatMap.put("tab", TabClickTime.class);

        authorTypeMap.put("star", AuthorType.STAR);
        authorTypeMap.put("normal", AuthorType.NORMAL);
    }
}
