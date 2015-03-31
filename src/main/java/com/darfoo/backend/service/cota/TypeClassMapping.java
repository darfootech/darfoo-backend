package com.darfoo.backend.service.cota;

import com.darfoo.backend.model.auth.Feedback;
import com.darfoo.backend.model.cota.DanceGroupType;
import com.darfoo.backend.model.cota.DanceVideoType;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
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
    public static HashMap<String, Class> innerTypeClassMap = new HashMap<String, Class>();
    public static HashMap<String, Class> cacheResponseMap = new HashMap<String, Class>();
    public static HashMap<String, Class> clickCountStatMap = new HashMap<String, Class>();
    public static HashMap<String, Class> clickTimeStatMap = new HashMap<String, Class>();
    public static HashMap<DanceGroupType, Class> authorTypeClassMap = new HashMap<DanceGroupType, Class>();
    public static HashMap<DanceVideoType, DanceGroupType> danceVideoTypeDanceGroupTypeMap = new HashMap<DanceVideoType, DanceGroupType>();
    public static HashMap<DanceGroupType, String> authorTypeVideoTypeMap = new HashMap<DanceGroupType, String>();

    public static HashMap<DanceVideoType, String> danceVideoTypeNameMap = new HashMap<DanceVideoType, String>();
    public static HashMap<DanceGroupType, String> danceGroupTypeNameMap = new HashMap<DanceGroupType, String>();

    public static HashMap<String, DanceVideoType> danceVideoTypeMap = new HashMap<String, DanceVideoType>();
    public static HashMap<String, DanceGroupType> danceGroupTypeMap = new HashMap<String, DanceGroupType>();

    static {
        typeClassMap.put("dancevideo", DanceVideo.class);
        typeClassMap.put("dancemusic", DanceMusic.class);
        typeClassMap.put("dancegroup", DanceGroup.class);

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

        innerTypeClassMap.put("dancevideo", DanceVideoType.class);
        innerTypeClassMap.put("dancegroup", DanceGroupType.class);

        cacheResponseMap.put("video", SingleVideo.class);
        cacheResponseMap.put("tutorial", SingleVideo.class);
        cacheResponseMap.put("music", SingleMusic.class);
        cacheResponseMap.put("author", SingleAuthor.class);

        clickCountStatMap.put("menu", MenuClickCount.class);
        clickCountStatMap.put("tab", TabClickCount.class);

        clickTimeStatMap.put("menu", MenuClickTime.class);
        clickTimeStatMap.put("tab", TabClickTime.class);

        authorTypeClassMap.put(DanceGroupType.STAR, DanceVideo.class);
        authorTypeClassMap.put(DanceGroupType.NORMAL, DanceVideo.class);

        danceVideoTypeDanceGroupTypeMap.put(DanceVideoType.NORMAL, DanceGroupType.NORMAL);
        danceVideoTypeDanceGroupTypeMap.put(DanceVideoType.TUTORIAL, DanceGroupType.STAR);

        authorTypeVideoTypeMap.put(DanceGroupType.NORMAL, "video");
        authorTypeVideoTypeMap.put(DanceGroupType.STAR, "tutorial");

        danceVideoTypeNameMap.put(DanceVideoType.NORMAL, "欣赏视频");
        danceVideoTypeNameMap.put(DanceVideoType.TUTORIAL, "教学视频");
        danceGroupTypeNameMap.put(DanceGroupType.NORMAL, "普通舞队");
        danceGroupTypeNameMap.put(DanceGroupType.STAR, "明星舞队");

        danceVideoTypeMap.put("normal", DanceVideoType.NORMAL);
        danceVideoTypeMap.put("tutorial", DanceVideoType.TUTORIAL);
        danceGroupTypeMap.put("normal", DanceGroupType.NORMAL);
        danceGroupTypeMap.put("star", DanceGroupType.STAR);
    }
}
