package com.eryansky.modules.sys.utils;

import com.eryansky.common.utils.Identities;
import com.eryansky.common.utils.SysConstants;
import com.google.common.collect.Maps;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


/**
 * 上传文件工具类
 * 
 * 数据字典工具类
 * @author : 尔演&Eryan eryanwcp@gmail.com
 * @date : 2014-05-17 21:22
 */
public class FileUploadUtils {
	 //默认大小 10M
    public static final long DEFAULT_MAX_SIZE = 10485760;
	 /**
	 * 文件上传
	 */
	public static Map<String, Object> upload(HttpServletRequest request,HttpServletResponse response) {
		String savePath = request.getSession().getServletContext().getRealPath("/")+ SysConstants.getUploadDirectory() + "/";// 文件保存目录路径
		String saveUrl = "";// 文件保存目录URL

		String contentDisposition = request.getHeader("Content-Disposition");// 如果是HTML5上传文件，那么这里有相应头的
		if (contentDisposition != null) {// HTML5拖拽上传文件
			Long fileSize = Long.valueOf(request.getHeader("Content-Length"));// 上传的文件大小
			String fileName = contentDisposition.substring(contentDisposition
					.lastIndexOf("filename=\""));// 文件名称
			fileName = fileName.substring(fileName.indexOf("\"") + 1);
			fileName = fileName.substring(0, fileName.indexOf("\""));

			ServletInputStream inputStream;
			try {
				inputStream = request.getInputStream();
			} catch (IOException e) {
				return uploadError("上传文件出错！");
			}

			if (inputStream == null) {
                return uploadError("您没有上传任何文件！");
			}

			if (fileSize > SysConstants.getUploadFileMaxSize()) {
                return uploadError("上传文件超出限制大小！", fileName);
			}

			// 检查文件扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();
			if (!Arrays.<String> asList(
					SysConstants.getUploadFileExts().split(",")).contains(
					fileExt)) {
                return uploadError("上传文件扩展名是不允许的扩展名。\n只允许"
						+ SysConstants.getUploadFileExts() + "格式！");
			}

			savePath += fileExt + "/";
			saveUrl += fileExt + "/";

			SimpleDateFormat yearDf = new SimpleDateFormat("yyyy");
			SimpleDateFormat monthDf = new SimpleDateFormat("MM");
			SimpleDateFormat dateDf = new SimpleDateFormat("dd");
			Date date = new Date();
			String ymd = yearDf.format(date) + "/" + monthDf.format(date) + "/"
					+ dateDf.format(date) + "/";
			savePath += ymd;
			saveUrl += ymd;

			File uploadDir = new File(savePath);// 创建要上传文件到指定的目录
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			String newFileName = UUID.randomUUID().toString()
					.replaceAll("-", "")
					+ "." + fileExt;// 新的文件名称
			File uploadedFile = new File(savePath, newFileName);

			try {
				FileCopyUtils.copy(inputStream, new FileOutputStream(
                        uploadedFile));
			} catch (FileNotFoundException e) {
                return uploadError("上传文件出错！");
			} catch (IOException e) {
                return uploadError("上传文件出错！");
			}

            return uploadSuccess(saveUrl+newFileName, fileName);// 文件上传成功
		}

        MultipartHttpServletRequest multiPartRequest = (MultipartHttpServletRequest) request;// 由于struts2上传文件时自动使用了request封装
        Map<String, MultipartFile> fileMap = multiPartRequest.getFileMap();// 上传的文件集合
        if (fileMap.size() == 0) {

            return uploadError("您没有上传任何文件！");
        }
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            String fileName = mf.getOriginalFilename();
            // 检查文件扩展名
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
                    .toLowerCase();

            savePath += fileExt + "/";
            saveUrl += fileExt + "/";

            SimpleDateFormat yearDf = new SimpleDateFormat("yyyy");
            SimpleDateFormat monthDf = new SimpleDateFormat("MM");
            SimpleDateFormat dateDf = new SimpleDateFormat("dd");
            Date date = new Date();
            String ymd = yearDf.format(date) + "/" + monthDf.format(date) + "/"
                    + dateDf.format(date) + "/";
            savePath += ymd;
            saveUrl += ymd;

            File uploadDir = new File(savePath);// 创建要上传文件到指定的目录
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String newFileName = Identities.uuid2()+"-" + fileName;// 新的文件名称
            File uploadedFile = new File(savePath, newFileName);

            try {
                FileCopyUtils.copy(mf.getBytes(), uploadedFile);// 利用spring的文件工具上传
            } catch (Exception e) {

                return uploadError("上传文件失败！", fileName);
            }

            return uploadSuccess(saveUrl + newFileName, fileName);// 文件上传成功
        }

        return null;
    }
	public static Map<String, Object> uploadError(String err, String msg) {
		Map<String, Object> m = Maps.newHashMap();
		m.put("err", err);
		m.put("msg", msg);
        return m;
	}

	public static Map<String, Object> uploadError(String err) {
		return uploadError(err, "");
	}
	public static Map<String, Object> uploadSuccess(String newFileName, String fileName) {
		Map<String, Object> m = Maps.newHashMap();
		m.put("err", "");
		Map<String, Object> nm = Maps.newHashMap();
		nm.put("url", newFileName);
		nm.put("localfile", fileName);
		m.put("msg", nm);
        return m;
	}
}
