package com.ck.it.controller.admin;

import com.ck.it.annotation.AutoFill;
import com.ck.it.constant.MessageConstant;
import com.ck.it.result.Result;
import com.ck.it.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.UUID;

/**
 * Package: com.ck.it.controller.admin
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 21:12
 */
@RestController
@RequestMapping("/admin/common")
@Tag(name="通用接口",description = "图片上传")
@Slf4j
public class CommonController {
	@Autowired
	private AliOssUtil aliOssUtil;

	@PostMapping("/upload")
	@Operation(summary = "图片上传")
	public Result<String> upload(MultipartFile file){
		log.info("文件上传：{}",file);
		try {
			// 原始文件名
			String originalFilename = file.getOriginalFilename();
			// 截取原始文件的后缀名
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			// 构造新文件名称
			String objectName = UUID.randomUUID().toString() + extension;
			// 文件的请求路径
			String filePath = aliOssUtil.upload(file.getBytes(), objectName);
			return Result.success(filePath);

		} catch (IOException e) {
			log.error("文件上传失败：{}",e);
		}
		return Result.error(MessageConstant.UPLOAD_FAILED);
	}

}
