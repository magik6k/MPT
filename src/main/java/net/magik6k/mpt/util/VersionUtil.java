package net.magik6k.mpt.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.magik6k.mpt.db.PackageBase;

import com.google.common.base.Charsets;

public class VersionUtil {
	private static MessageDigest md5;
	
	static{
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static String getNewHash(String pack){
		synchronized (md5) {
			md5.reset();
			md5.update(pack.getBytes(Charsets.UTF_8));
			return new BigInteger(md5.digest()).toString(16);
		}
	}
	
	public static String getPackageHash(String pack){
		synchronized (md5) {
			md5.reset();
			md5.update(pack.getBytes(Charsets.UTF_8));
			for(MptFile file : PackageBase.instance.getFiles(pack)){
				md5.update(file.name.getBytes(Charsets.UTF_8));
				md5.update(file.content.getBytes(Charsets.UTF_8));
			}
			return new BigInteger(md5.digest()).toString(16);
		}
	}
}
