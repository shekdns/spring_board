package org.zerock.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	
	
	//D:\\spring\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\spring_board\\resources\\upload
	@Value("${globalConfig.uploadPath}")
	private String uploadPath;
	//ファイル日付フォーマット
	private static String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);

		return str.replace("-", File.separator);
	}
	
	//イメージファイルかどうかチェック
	private static boolean checkImageType(File file) {

		try {
			String contentType = Files.probeContentType(file.toPath());

			return contentType.startsWith("image");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		
		log.info("upload ajax");
	}
	
	//ajaxファイルのアップロードのメソッド
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		
		
		List<AttachFileDTO> list = new ArrayList<>();
		
		log.info("update ajax post");
		
		//ファイル経路設定
		//String uploadFolder = "C:\\upload";
		String uploadFolder = uploadPath;
		
		//経路にgetFolerを通じて年月日フォルダー生成
		String uploadFolderPath = getFolder();
		
		//経路に新しいファイルを作成
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("upload path: " + uploadPath);
		
		//もし経路にフォルダがなければ新しく作成
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		//配列を通して値を受け取るので、for文を使用
		for(MultipartFile multipartFile : uploadFile) {
			
			//AttachFileDTOオブジェクト作成
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			//元のファイル名の取り込み
			String uploadFileName = multipartFile.getOriginalFilename();
			
			//ファイル名の後ろに番号をつける。
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			
			attachDTO.setFileName(uploadFileName);
			//UUIDを使用して重複値を防止
			UUID uuid = UUID.randomUUID();
			
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			
			//File saveFile = new File(uploadFolder, uploadFileName);
			
			//ファイル保存構文
			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);
				
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				//もしイメージファイルなら、サムネイルイメージ作成
				if (checkImageType(saveFile)) {
					
					attachDTO.setImage(true);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));

					
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}
				
				//リスト追加
				list.add(attachDTO);
				
			}catch (Exception e) {
				log.error(e.getMessage());
			}
			
		}// end for
		return new ResponseEntity<>(list, HttpStatus.OK);
		
	}
	
	//ファイルアップロード時に画面にファイルを表示する関数
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		
		log.info("fileName: " + fileName);
		
		//File file = new File("C:\\upload\\" + fileName);
		File file = new File(uploadPath + "\\" + fileName);
		
		log.info("file: " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			//バイトに変換して表示
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//ダウンロード関数
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName){
		
		//どのファイルなのか保存
		//Resource resource = new FileSystemResource("C:\\upload\\" + fileName);
		Resource resource = new FileSystemResource(uploadPath + "\\" + fileName);
	
		//存在しない場合は、NOT_FOUND リターン
		if(resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		//ファイルの名前持ってくること
		String resourceName = resource.getFilename();
		
		//UUID削除
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);
		
		//ダウンロード構文ブラウザによってencode方式が異なる
		HttpHeaders headers = new HttpHeaders();
		try {
			
			String downloadName = null;
			
			if(userAgent.contains("Trident")) {
				
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
			
			}else if(userAgent.contains("Edge")) {
				
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
				
				
			}else {
				//chrome
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			headers.add("Content-Disposition", "attachment; filename=" + downloadName);
		}catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource,headers, HttpStatus.OK);
	}
	
	//ファイル削除
	//アップロードされたファイルをサーバーから削除する。
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type){
		
		//初期値はNULL
		File file = null;
		
		try {
			//経路を探して削除する。
			//file = new File("c:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
			file = new File(uploadPath +"\\" + URLDecoder.decode(fileName, "UTF-8"));

			file.delete();
			
			if(type.equals("image")) {
				
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				
				file = new File(largeFileName);
				file.delete();
			}
			
		}catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("deleted", HttpStatus.OK);		
		
		
	}
	
}
