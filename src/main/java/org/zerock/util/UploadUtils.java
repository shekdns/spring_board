package org.zerock.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

/*
 * ファイルアップロードユーティリティクラス
 * 
 */
@Controller
@Log4j
public class UploadUtils {

	private static String getFolder() {
		
		//ファイル日付フォーマット
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

	@PostMapping("/uploadFormAction")
	public static String uploadFormPost(MultipartFile uploadFile, String realUploadPath) {

		String uploadFolder = realUploadPath; // image経路
		String saveFileName = null; // サーバーに保存されるファイル名
		String fullSaveName = null; // uploadFolder + saveFileName
		String savePath = getFolder(); //日付別に生成された経路を含める。 例)20200101
		
		// make folder --------
		File uploadPath = new File(uploadFolder, getFolder());
		log.info("upload path: " + uploadPath);

		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}

		if (uploadFile.getSize() > 0) {

			String uploadFileName = uploadFile.getOriginalFilename();

			// ファイル名の後ろに順番をつける。
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name: " + uploadFileName);
			
			//重複防止
			//ファイル名を作成する時に同じ名前でアップロードすると、
			//既存のファイルを削除するので、UUIDユーティールを使用
			UUID uuid = UUID.randomUUID();
			
			//ランダム値の後ろにファイル名が付く
			saveFileName = uuid.toString() + "_" + uploadFileName;
			
			try {
				File saveFile = new File(uploadPath, saveFileName);
				uploadFile.transferTo(saveFile);

				// uploadされたファイルがイメージの場合はサムネイルを製作
				if (checkImageType(saveFile)) {

					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + saveFileName));

					//サムネイルサイズを指定する。 プロジェクトによってサムネイルの大きさを調節して使用
					Thumbnailator.createThumbnail(uploadFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}
				
				log.info("uploadFileName : " + uploadFileName);
				log.info("saveFileName : " + saveFileName);
				log.info("uploadPath : " + uploadPath);
				
				fullSaveName = savePath.replace("\\", "/") + "/" + saveFileName;
				
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return fullSaveName;
	}

}
