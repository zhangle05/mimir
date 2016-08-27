/**
 * 
 */
package com.jotunheim.mimir.web.admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jotunheim.mimir.dao.PhotoDao;
import com.jotunheim.mimir.domain.Photo;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.domain.data.RoleAccessLevel;
import com.jotunheim.mimir.web.annotation.Login;
import com.jotunheim.mimir.web.utils.SharedConstants;

/**
 * @author zhangle
 *
 */
@RequestMapping("/admin/photo")
@Controller
@Login(role = RoleAccessLevel.ADMIN)
public class PhotoAdminController {

    private static Log LOG = LogFactory.getLog(PhotoAdminController.class);

    public static String UPLOAD_ROOT = "upload-dir";

    @Autowired
    private PhotoDao photoDao;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html")
    public String adminHome(Model uiModel, HttpServletRequest request) {
        LOG.debug("photo admin home.");
        return "redirect:/admin/index";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "text/html")
    public String listPhotoes(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "page", required = false) Integer page) {
        LOG.debug("get photo list.");
        UserRole role = (UserRole) request.getSession()
                .getAttribute("userRole");
        LOG.debug("role is:" + role);
        if (role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        if (page == null) {
            page = 0;
        }
        uiModel.addAttribute("page", page);
        uiModel.addAttribute("count", photoDao.getPhotoCount());
        uiModel.addAttribute("photoes",
                photoDao.listPhotos(page, SharedConstants.DEFAULT_PAGE_SIZE));
        return "admin/photo_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET, produces = "text/html")
    public String addPhotoForm(Model uiModel, HttpServletRequest request) {
        LOG.debug("create add-photo form.");
        UserRole role = (UserRole) request.getSession()
                .getAttribute("userRole");
        LOG.debug("role is:" + role);
        if (role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        uiModel.addAttribute("returnUrl", "/admin/photo/list");
        return "admin/add_photo";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String addPhoto(HttpServletRequest request,
            @Valid Photo photo, BindingResult bindingResult,
            HttpServletRequest httpServletRequest) {
        LOG.debug("add photo");
        JSONObject json = new JSONObject();
        UserRole role = (UserRole) request.getSession()
                .getAttribute("userRole");
        LOG.debug("role is:" + role);
        if (role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_ROLE_ACCESS_LEVEL_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY,
                    "Only the administrator can add photoes.");
            return json.toString();
        }
        if (StringUtils.isEmpty(photo.getTitle())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "标题不能为空!");
            return json.toString();
        }
        if (StringUtils.isEmpty(photo.getUri())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "URL不能为空!");
            return json.toString();
        }
        try {
            if (StringUtils.isEmpty(photo.getDescription())) {
                photo.setDescription(photo.getTitle());
            }
            if (StringUtils.isEmpty(photo.getThumbnail())) {
                photo.setThumbnail(photo.getUri());
            }
            photo.setCreateTime(new java.util.Date());
            photoDao.persist(photo);
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_OK);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY,
                    ex + "-" + ex.getMessage());
        }
        return json.toString();
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET, produces = "text/html")
    public String deletePhoto(Model uiModel, @Valid Photo photo,
            BindingResult bindingResult, HttpServletRequest request) {
        LOG.debug("deleting photo.");
        UserRole role = (UserRole) request.getSession()
                .getAttribute("userRole");
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        LOG.debug("role is:" + role);
        if (role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        Photo realPhoto = photoDao.findById(photo.getId());
        if (realPhoto == null) {
            uiModel.addAttribute("errorMsg", "photo'" + photo.getId() + "'不存在!");
            return "uncaught_exception";
        }
        photoDao.delete(realPhoto);
        return "redirect:/admin/photo/list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET, produces = "text/html")
    public String updatePhotoForm(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "pid", required = true) Long photoId) {
        LOG.debug("create edit-photo form.");
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        UserRole role = (UserRole) request.getSession()
                .getAttribute("userRole");
        LOG.debug("role is:" + role);
        if (role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        Photo p = photoDao.findById(photoId);
        if (p == null) {
            uiModel.addAttribute("errorMsg", "没找到'" + photoId + "'对应的photo!");
            return "uncaught_exception";
        }
        LOG.info("photo found:" + p.getTitle());
        uiModel.addAttribute("photo", p);
        return "admin/edit_photo";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String editPhoto(Model uiModel,
            HttpServletRequest request, @Valid Photo photo,
            BindingResult bindingResult) {
        LOG.debug("edit photo");
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        UserRole role = (UserRole) request.getSession()
                .getAttribute("userRole");
        LOG.debug("role is:" + role);
        if (role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        JSONObject json = new JSONObject();
        LOG.debug("photo id is:" + photo.getId());
        Photo realPhoto = photoDao.findById(photo.getId());
        if (realPhoto == null) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY,
                    "没找到'" + photo.getId() + "'对应的photo!");
            return json.toString();
        }
        if (StringUtils.isEmpty(photo.getTitle())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "标题不能为空!");
            return json.toString();
        }
        if (StringUtils.isEmpty(photo.getUri())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "URL不能为空!");
            return json.toString();
        }
        try {
            LOG.debug("========= fields to edit: ==================");
            LOG.debug("title: " + photo.getTitle());
            realPhoto.setTitle(photo.getTitle());
            LOG.debug("URL:" + photo.getUri());
            realPhoto.setUri(photo.getUri());
            if (!StringUtils.isEmpty(photo.getDescription())) {
                realPhoto.setDescription(photo.getDescription());
            }
            if (!StringUtils.isEmpty(photo.getThumbnail())) {
                realPhoto.setThumbnail(photo.getThumbnail());
            }
            photoDao.attachDirty(realPhoto);
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_OK);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            json.accumulate(SharedConstants.AJAX_CODE_KEY,
                    SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY,
                    ex + "-" + ex.getMessage());
        }
        return json.toString();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String handleFileUpload(
            Model uiModel,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "tags", required = true) String tags,
            @RequestParam(value = "cTime", required = false) String createTimeStr,
            @RequestParam(value = "desc", required = false) String description) {
        LOG.info("do upload, file:" + file.getOriginalFilename() + ",name:"
                + name + ",tags:" + tags + ",cTime:" + createTimeStr + ",desc:"
                + description);
        JSONObject json = new JSONObject();
        User user = (User)request.getSession().getAttribute("loginUser");
        if (user == null) {
            json.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_AUTHENTICATION_ERROR);
            json.put(SharedConstants.AJAX_MSG_KEY, "Need login.");
            return json.toString();
        }
        if (!file.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String folder = UPLOAD_ROOT + "/"
                        + sdf.format(new java.util.Date());
                File dir = new File(folder);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String originalName = file.getOriginalFilename();
                String extension = originalName.substring(originalName
                        .lastIndexOf('.'));
                String uuid = UUID.randomUUID().toString();
                String newName = uuid + extension;
                Files.copy(file.getInputStream(), Paths.get(folder, newName));

                Photo p = new Photo();
                p.setCreateTime(new java.util.Date());
                if (!StringUtils.isEmpty(createTimeStr)) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        p.setCreateTime(sdf.parse(createTimeStr));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                p.setDescription(description);
                p.setTitle(name);
                p.setOwnerId(user.getId());
                p.setTags(tags);
                p.setUri("/" + folder + "/" + newName);
                photoDao.persist(p);
                json.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
                json.put(SharedConstants.AJAX_MSG_KEY, "ok.");
            } catch (Exception e) {
                e.printStackTrace();
                json.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
                json.put(SharedConstants.AJAX_MSG_KEY, e.getMessage());
            }
        } else {
            json.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.put(SharedConstants.AJAX_MSG_KEY, "File is empty.");
        }
        return json.toString();
    }

}
