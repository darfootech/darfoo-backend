package com.darfoo.backend.service.cota;

import com.darfoo.backend.model.Advertise;
import com.darfoo.backend.model.Version;
import com.darfoo.backend.model.auth.Feedback;
import com.darfoo.backend.model.cota.enums.*;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaSeries;
import com.darfoo.backend.model.resource.opera.OperaVideo;
import com.darfoo.backend.model.statistics.CrashLog;
import com.darfoo.backend.model.statistics.DanceSearchHistory;
import com.darfoo.backend.model.statistics.clickcount.MenuClickCount;
import com.darfoo.backend.model.statistics.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.clickcount.TabClickCount;
import com.darfoo.backend.model.statistics.clicktime.MenuClickTime;
import com.darfoo.backend.model.statistics.clicktime.ResourceClickTime;
import com.darfoo.backend.model.statistics.clicktime.TabClickTime;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
import com.darfoo.backend.service.responsemodel.*;

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
    public static HashMap<Integer, String> menuTitleMap = new HashMap<Integer, String>();
    public static HashMap<Integer, String> tabTitleMap = new HashMap<Integer, String>();

    public static HashMap<DanceGroupType, DanceVideoType> danceGroupTypeDanceVideoTypeMap = new HashMap<DanceGroupType, DanceVideoType>();
    public static HashMap<DanceVideoType, DanceGroupType> danceVideoTypeDanceGroupTypeMap = new HashMap<DanceVideoType, DanceGroupType>();

    public static HashMap<DanceVideoType, String> danceVideoTypeNameMap = new HashMap<DanceVideoType, String>();
    public static HashMap<DanceGroupType, String> danceGroupTypeNameMap = new HashMap<DanceGroupType, String>();
    public static HashMap<OperaVideoType, String> operaVideoTypeNameMap = new HashMap<OperaVideoType, String>();
    public static HashMap<String, String> versionTypeNameMap = new HashMap<String, String>();

    public static HashMap<String, DanceVideoType> danceVideoTypeMap = new HashMap<String, DanceVideoType>();
    public static HashMap<String, DanceGroupType> danceGroupTypeMap = new HashMap<String, DanceGroupType>();

    public static HashMap<String, OperaVideoType> operaVideoTypeMap = new HashMap<String, OperaVideoType>();

    public static HashMap<Enum, String> typeNameMap = new HashMap<Enum, String>();

    public static HashMap<String, Class> resourceFieldClassMap = new HashMap<String, Class>();

    public static HashMap<String, String> typeNameLiteralMap = new HashMap<String, String>();

    public static HashMap<String, String> fieldNameLiteralMap = new HashMap<String, String>();

    static {
        typeClassMap.put("dancevideo", DanceVideo.class);
        typeClassMap.put("dancemusic", DanceMusic.class);
        typeClassMap.put("dancegroup", DanceGroup.class);
        typeClassMap.put("operavideo", OperaVideo.class);
        typeClassMap.put("operaseries", OperaSeries.class);
        typeClassMap.put("advertise", Advertise.class);
        typeClassMap.put("version", Version.class);

        typeClassMap.put("uploadnoauthvideo", UploadNoAuthVideo.class);
        typeClassMap.put("resourceclickcount", ResourceClickCount.class);
        typeClassMap.put("menuclickcount", MenuClickCount.class);
        typeClassMap.put("tabclickcount", TabClickCount.class);
        typeClassMap.put("resourceclicktime", ResourceClickTime.class);
        typeClassMap.put("menuclicktime", MenuClickTime.class);
        typeClassMap.put("tabclicktime", TabClickTime.class);
        typeClassMap.put("searchhistory", DanceSearchHistory.class);
        typeClassMap.put("feedback", Feedback.class);
        typeClassMap.put("crashlog", CrashLog.class);

        innerTypeClassMap.put("dancevideo", DanceVideoType.class);
        innerTypeClassMap.put("dancegroup", DanceGroupType.class);
        innerTypeClassMap.put("operavideo", OperaVideoType.class);

        cacheResponseMap.put("dancevideo", SingleDanceVideo.class);
        cacheResponseMap.put("dancemusic", SingleDanceMusic.class);
        cacheResponseMap.put("dancegroup", SingleDanceGroup.class);
        cacheResponseMap.put("operavideo", SingleOperaVideo.class);
        cacheResponseMap.put("operaseries", SingleOperaSeries.class);
        cacheResponseMap.put("advertise", SingleAdvertise.class);

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
        operaVideoTypeNameMap.put(OperaVideoType.SERIES, "越剧连续剧");
        operaVideoTypeNameMap.put(OperaVideoType.SINGLE, "越剧电影");
        versionTypeNameMap.put("release", "发布版本");
        versionTypeNameMap.put("debug", "调试版本");

        typeNameMap.put(DanceVideoType.NORMAL, "欣赏视频");
        typeNameMap.put(DanceVideoType.TUTORIAL, "教学视频");
        typeNameMap.put(DanceGroupType.NORMAL, "普通舞队");
        typeNameMap.put(DanceGroupType.STAR, "明星舞队");
        typeNameMap.put(OperaVideoType.SERIES, "越剧连续剧");
        typeNameMap.put(OperaVideoType.SINGLE, "越剧电影");

        typeNameLiteralMap.put("dancemusic", "舞蹈伴奏");
        typeNameLiteralMap.put("dancegroup", "舞队");

        fieldNameLiteralMap.put("hot", "热门");
        fieldNameLiteralMap.put("priority", "高优先级");

        danceVideoTypeMap.put("normal", DanceVideoType.NORMAL);
        danceVideoTypeMap.put("tutorial", DanceVideoType.TUTORIAL);
        danceGroupTypeMap.put("normal", DanceGroupType.NORMAL);
        danceGroupTypeMap.put("star", DanceGroupType.STAR);
        operaVideoTypeMap.put("series", OperaVideoType.SERIES);
        operaVideoTypeMap.put("single", OperaVideoType.SINGLE);

        resourceFieldClassMap.put("hot", ResourceHot.class);
        resourceFieldClassMap.put("priority", ResourcePriority.class);

        menuTitleMap.put(1, "广场舞");
        menuTitleMap.put(2, "越剧");
        menuTitleMap.put(3, "电视剧");
        menuTitleMap.put(4, "游戏");
        menuTitleMap.put(5, "聊天");
        menuTitleMap.put(6, "本地管理");
        menuTitleMap.put(7, "链接上网");
        menuTitleMap.put(8, "设置");

        tabTitleMap.put(1, "推荐");
        tabTitleMap.put(2, "舞队");
        tabTitleMap.put(3, "教学");
        tabTitleMap.put(4, "欣赏");
        tabTitleMap.put(5, "伴奏");
        tabTitleMap.put(6, "本地");
        tabTitleMap.put(7, "搜索");
    }
}
