package com.darfoo.backend.service.cota;

import com.darfoo.backend.model.auth.Feedback;
import com.darfoo.backend.model.cota.enums.DanceGroupType;
import com.darfoo.backend.model.cota.enums.DanceVideoType;
import com.darfoo.backend.model.cota.enums.ResourceHot;
import com.darfoo.backend.model.cota.enums.ResourcePriority;
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
import com.darfoo.backend.service.responsemodel.SingleDanceGroup;
import com.darfoo.backend.service.responsemodel.SingleDanceMusic;
import com.darfoo.backend.service.responsemodel.SingleDanceVideo;

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

    public static HashMap<DanceGroupType, DanceVideoType> danceGroupTypeDanceVideoTypeMap = new HashMap<DanceGroupType, DanceVideoType>();
    public static HashMap<DanceVideoType, DanceGroupType> danceVideoTypeDanceGroupTypeMap = new HashMap<DanceVideoType, DanceGroupType>();

    public static HashMap<DanceVideoType, String> danceVideoTypeNameMap = new HashMap<DanceVideoType, String>();
    public static HashMap<DanceGroupType, String> danceGroupTypeNameMap = new HashMap<DanceGroupType, String>();

    public static HashMap<String, DanceVideoType> danceVideoTypeMap = new HashMap<String, DanceVideoType>();
    public static HashMap<String, DanceGroupType> danceGroupTypeMap = new HashMap<String, DanceGroupType>();

    public static HashMap<String, Class> resourceFieldClassMap = new HashMap<String, Class>();

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

        cacheResponseMap.put("dancevideo", SingleDanceVideo.class);
        cacheResponseMap.put("dancemusic", SingleDanceMusic.class);
        cacheResponseMap.put("dancegroup", SingleDanceGroup.class);

        clickCountStatMap.put("menu", MenuClickCount.class);
        clickCountStatMap.put("tab", TabClickCount.class);

        clickTimeStatMap.put("menu", MenuClickTime.class);
        clickTimeStatMap.put("tab", TabClickTime.class);

        danceGroupTypeDanceVideoTypeMap.put(DanceGroupType.NORMAL, DanceVideoType.NORMAL);
        danceGroupTypeDanceVideoTypeMap.put(DanceGroupType.STAR, DanceVideoType.TUTORIAL);
        danceVideoTypeDanceGroupTypeMap.put(DanceVideoType.NORMAL, DanceGroupType.NORMAL);
        danceVideoTypeDanceGroupTypeMap.put(DanceVideoType.TUTORIAL, DanceGroupType.STAR);

        danceVideoTypeNameMap.put(DanceVideoType.NORMAL, "欣赏视频");
        danceVideoTypeNameMap.put(DanceVideoType.TUTORIAL, "教学视频");
        danceGroupTypeNameMap.put(DanceGroupType.NORMAL, "普通舞队");
        danceGroupTypeNameMap.put(DanceGroupType.STAR, "明星舞队");

        danceVideoTypeMap.put("normal", DanceVideoType.NORMAL);
        danceVideoTypeMap.put("tutorial", DanceVideoType.TUTORIAL);
        danceGroupTypeMap.put("normal", DanceGroupType.NORMAL);
        danceGroupTypeMap.put("star", DanceGroupType.STAR);

        resourceFieldClassMap.put("hot", ResourceHot.class);
        resourceFieldClassMap.put("priority", ResourcePriority.class);
    }
}
