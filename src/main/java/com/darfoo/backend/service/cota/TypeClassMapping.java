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
    public static HashMap<AuthorType, Class> authorTypeClassMap = new HashMap<AuthorType, Class>();
    public static HashMap<String, AuthorType> videoTypeAuthorTypeMap = new HashMap<String, AuthorType>();
    public static HashMap<AuthorType, String> authorTypeVideoTypeMap = new HashMap<AuthorType, String>();
    public static HashMap<AuthorType, String> authorTypeNameMap = new HashMap<AuthorType, String>();

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

        authorTypeClassMap.put(AuthorType.STAR, Tutorial.class);
        authorTypeClassMap.put(AuthorType.NORMAL, Video.class);

        videoTypeAuthorTypeMap.put("video", AuthorType.NORMAL);
        videoTypeAuthorTypeMap.put("tutorial", AuthorType.STAR);

        authorTypeVideoTypeMap.put(AuthorType.NORMAL, "video");
        authorTypeVideoTypeMap.put(AuthorType.STAR, "tutorial");

        authorTypeNameMap.put(AuthorType.NORMAL, "普通舞队");
        authorTypeNameMap.put(AuthorType.STAR, "明星舞队");
    }
}
