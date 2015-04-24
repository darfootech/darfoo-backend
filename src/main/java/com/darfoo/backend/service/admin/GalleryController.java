package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.Advertise;
import com.darfoo.backend.model.Version;
import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.category.DanceVideoCategory;
import com.darfoo.backend.model.cota.enums.DanceVideoType;
import com.darfoo.backend.model.cota.enums.OperaVideoType;
import com.darfoo.backend.model.cota.enums.ResourcePriority;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaSeries;
import com.darfoo.backend.model.resource.opera.OperaVideo;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
import com.darfoo.backend.service.category.DanceMusicCates;
import com.darfoo.backend.service.category.DanceVideoCates;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.QiniuResourceEnum;
import com.darfoo.backend.utils.QiniuUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zjh on 14-12-4.
 * 用于展示已经上传的伴奏，视频和教学视频，全部或者是单个
 */

@Controller
public class GalleryController {
    @Autowired
    CommonDao commonDao;
    @Autowired
    QiniuUtils qiniuUtils;

    private ModelMap getResourceModelMap(Class resource, Integer id, ModelMap modelMap) {
        Object object = commonDao.getResourceById(resource, id);
        if (resource == DanceVideo.class) {
            DanceVideo video = (DanceVideo) object;
            Set<DanceVideoCategory> categories = video.getCategories();
            Set<String> categoryTitles = new HashSet<String>();
            for (DanceVideoCategory category : categories) {
                categoryTitles.add(category.getTitle());
            }
            HashMap<String, String> bindCategories = new HashMap<String, String>();
            HashMap<String, String> notBindCategories = new HashMap<String, String>();
            for (String key : DanceVideoCates.danceVideoCategoryMap.keySet()) {
                String category = DanceVideoCates.danceVideoCategoryMap.get(key);
                if (categoryTitles.contains(category)) {
                    bindCategories.put(key, category);
                } else {
                    notBindCategories.put(key, category);
                }
            }
            modelMap.addAttribute("bindcategories", bindCategories);
            modelMap.addAttribute("notbindcategories", notBindCategories);
        } else if (resource == DanceMusic.class) {
            DanceMusic music = (DanceMusic) object;
            Set<DanceMusicCategory> categories = music.getCategories();
            for (DanceMusicCategory category : categories) {
                String categorytitle = category.getTitle();
                if (DanceMusicCates.letterCategories.containsValue(categorytitle)) {
                    modelMap.addAttribute("letter", categorytitle);
                }
            }
        } else if (resource == UploadNoAuthVideo.class) {
            String videokey = commonDao.getResourceAttr(resource, object, "video_key").toString();
            modelMap.addAttribute("video", object);
            modelMap.addAttribute("videourl", qiniuUtils.getQiniuResourceUrl(videokey, QiniuResourceEnum.RAWNORMAL));
        } else {
            System.out.println("wired");
        }

        if (resource == DanceVideo.class) {
            modelMap = putVideoLikeResourceToModelMap(resource, object, modelMap);
            DanceVideoType danceVideoType = (DanceVideoType) commonDao.getResourceAttr(resource, object, "type");
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("type", TypeClassMapping.danceVideoTypeDanceGroupTypeMap.get(danceVideoType));
            modelMap.addAttribute("authors", commonDao.getResourcesByFields(DanceGroup.class, conditions));

            DanceMusic music = (DanceMusic) commonDao.getResourceAttr(resource, object, "music");
            if (music != null) {
                String connectmusic = music.getTitle() + "-" + music.getAuthorname();
                modelMap.addAttribute("connectmusic", connectmusic);
            } else {
                modelMap.addAttribute("connectmusic", "请输入要关联的伴奏并选择");
            }
        }

        if (resource == OperaVideo.class) {
            modelMap = putVideoLikeResourceToModelMap(resource, object, modelMap);
            OperaVideoType type = (OperaVideoType) commonDao.getResourceAttr(resource, object, "type");
            if (type == OperaVideoType.SERIES) {
                modelMap.addAttribute("serieses", commonDao.getAllResource(OperaSeries.class));
            }
        }

        if (resource == DanceMusic.class) {
            String musickey = commonDao.getResourceAttr(resource, object, "music_key").toString();
            modelMap.addAttribute("musicurl", qiniuUtils.getQiniuResourceUrl(musickey, QiniuResourceEnum.RAWNORMAL));
            modelMap.addAttribute("music", object);
        }

        if (resource == DanceGroup.class) {
            modelMap.addAttribute("author", object);
            modelMap = putImageToModelMap(resource, object, modelMap);
        }

        if (resource == OperaSeries.class) {
            modelMap.addAttribute("series", object);
            modelMap = putImageToModelMap(resource, object, modelMap);
        }

        if (resource == Advertise.class) {
            modelMap.addAttribute(resource.getSimpleName().toLowerCase(), object);
            modelMap = putImageToModelMap(resource, object, modelMap);
        }

        if (resource == Version.class) {
            modelMap.addAttribute(resource.getSimpleName().toLowerCase(), object);
        }

        return modelMap;
    }

    public ModelMap putImageToModelMap(Class resource, Object object, ModelMap modelMap) {
        Image image = (Image) commonDao.getResourceAttr(resource, object, "image");
        if (image != null) {
            modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrl(image.getImage_key(), QiniuResourceEnum.RAWNORMAL));
            if (image.getImage_key().equals("")) {
                modelMap.addAttribute("updateauthorimage", 1);
            }
        } else {
            modelMap.addAttribute("imageurl", "");
        }
        return modelMap;
    }

    public ModelMap putVideoLikeResourceToModelMap(Class resource, Object object, ModelMap modelMap) {
        modelMap.addAttribute("video", object);
        modelMap.addAttribute("innertype", commonDao.getResourceAttr(resource, object, "type"));
        String videokey = commonDao.getResourceAttr(resource, object, "video_key").toString();
        String imagekey = ((Image) commonDao.getResourceAttr(resource, object, "image")).getImage_key();
        modelMap.addAttribute("videourl", qiniuUtils.getQiniuResourceUrl(videokey, QiniuResourceEnum.RAWNORMAL));
        modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrl(imagekey, QiniuResourceEnum.RAWNORMAL));
        return modelMap;
    }

    public String galleryAllResources(List resources, ModelMap modelMap, String type) {
        modelMap.addAttribute("resources", resources);
        modelMap.addAttribute("type", type);
        return "resource/allresources";
    }

    @RequestMapping(value = "/admin/gallery/all/{type}/{innertype}", method = RequestMethod.GET)
    public String showAllResourcesByInnerType(@PathVariable String type, @PathVariable String innertype, ModelMap modelMap) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        if (type.equals("version")) {
            conditions.put("type", innertype);
        } else {
            conditions.put("type", Enum.valueOf(TypeClassMapping.innerTypeClassMap.get(type), innertype));
        }
        List resources = commonDao.getResourcesByFields(TypeClassMapping.typeClassMap.get(type), conditions);
        return galleryAllResources(resources, modelMap, type);
    }

    @RequestMapping(value = "/admin/gallery/{type}/all", method = RequestMethod.GET)
    public String showAllResource(@PathVariable String type, ModelMap modelMap) {
        if (type.equals("dancegroup")) {
            modelMap.put("typenames", TypeClassMapping.danceGroupTypeNameMap);
        } else if (type.equals("dancevideo")) {
            modelMap.put("typenames", TypeClassMapping.danceVideoTypeNameMap);
        } else if (type.equals("operavideo")) {
            modelMap.put("typenames", TypeClassMapping.operaVideoTypeNameMap);
        } else if (type.equals("version")) {
            modelMap.put("typenames", TypeClassMapping.versionTypeNameMap);
        } else {
            List resources = commonDao.getAllResource(TypeClassMapping.typeClassMap.get(type));
            return galleryAllResources(resources, modelMap, type);
        }
        modelMap.addAttribute("operation", "manage");
        modelMap.put("type", type);
        return "resource/resourcetype";
    }

    @RequestMapping(value = "/admin/{type}/{id}", method = RequestMethod.GET)
    public String showSingleResource(@PathVariable String type, @PathVariable Integer id, HttpServletRequest request, ModelMap modelMap) {
        getResourceModelMap(TypeClassMapping.typeClassMap.get(type), id, modelMap);
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("type", type);
        modelMap.addAttribute("role", request.getSession().getAttribute("loginUser"));
        return String.format("resource/single%s", type);
    }

    @RequestMapping(value = "/admin/{type}/videos/{id}", method = RequestMethod.GET)
    public String showAllVideos(@PathVariable String type, @PathVariable Integer id, ModelMap modelMap) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        if (type.equals("dancegroup")) {
            conditions.put("author_id", id);
            List videos = commonDao.getResourcesByFields(DanceVideo.class, conditions);
            return galleryAllResources(videos, modelMap, "dancevideo");
        }
        if (type.equals("operaseries")) {
            conditions.put("series_id", id);
            List videos = commonDao.getResourcesByFields(OperaVideo.class, conditions);
            return galleryAllResources(videos, modelMap, "operavideo");
        }
        return "fail";
    }

    //设置越剧连续剧中的越剧电影的顺序
    @RequestMapping(value = "/admin/{type}/videos/order/{id}", method = RequestMethod.GET)
    public String setResourceVideosOrder(@PathVariable String type, @PathVariable Integer id, ModelMap modelMap) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        if (type.equals("operaseries")) {
            conditions.put("series_id", id);
            List videos = commonDao.getResourcesByFieldsByOrder(OperaVideo.class, conditions, Order.asc("order"));
            List<Integer> orders = new ArrayList<Integer>();
            for (int i = 0; i < videos.size(); i++) {
                orders.add(i + 1);
            }
            modelMap.addAttribute("resources", videos);
            modelMap.addAttribute("type", "operavideo");
            modelMap.addAttribute("orders", orders);
            modelMap.addAttribute("title", "设置越剧连续剧关联越剧视频的显示顺序");
            return "update/setresourceorder";
        }
        return "fail";
    }

    @RequestMapping(value = "/admin/{type}/set/order", method = RequestMethod.GET)
    public String setResourceOrder(@PathVariable String type, ModelMap modelMap) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        if (resource == DanceGroup.class) {
            conditions.put("priority", ResourcePriority.ISPRIORITY);
        }
        if (conditions.isEmpty()) {
            return "fail";
        }
        List resources = commonDao.getResourcesByFieldsByOrder(resource, conditions, Order.asc("order"));
        List<Integer> orders = new ArrayList<Integer>();
        for (int i = 0; i < resources.size(); i++) {
            orders.add(i + 1);
        }
        modelMap.addAttribute("resources", resources);
        modelMap.addAttribute("type", type);
        modelMap.addAttribute("orders", orders);
        modelMap.addAttribute("title", String.format("设置%s的现实顺序", TypeClassMapping.typeNameLiteralMap.get(type)));
        return "update/setresourceorder";
    }
}