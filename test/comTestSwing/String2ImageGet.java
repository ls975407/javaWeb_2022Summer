

import java.util.ArrayList;
import java.util.List;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.test.ImageGetAd;
import java.io.IOException;

public class String2ImageGet {

	public final ImageGet _imageGet;
	public final FileBS _fileBS;
	public String2ImageGet(String[] localPaths) throws IOException {
		List<FileB> fileBs = new ArrayList<>();
		for (String localPath: localPaths) {
			fileBs.add(new FileB(FileM.read(localPath)));
		}
		_fileBS = new FileBS(fileBs.toArray(new FileB[0]));
		_imageGet = new ImageGetAd(_fileBS);	
	}
}

