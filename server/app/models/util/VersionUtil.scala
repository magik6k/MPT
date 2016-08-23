package models.util

import java.math.BigInteger
import java.security.MessageDigest

import com.google.common.base.Charsets
import models.Database

object VersionUtil {
  val md5 = MessageDigest.getInstance("md5")

  def getNewHash(pack: String): String = {
    md5 synchronized {
      md5.reset()
      md5.update(pack.getBytes(Charsets.UTF_8))
      new BigInteger(md5.digest()).toString(16)
    }
  }

  def getPackageHash(pack: String): String = {
    md5 synchronized {
      md5.reset()
      md5.update(pack.getBytes(Charsets.UTF_8))
      val files = Database.packageFiles(pack)
      files.foreach(f => md5.update(f.getBytes(Charsets.UTF_8)))
      files.map(f => Database.getPackageFile(pack, f)).foreach(f => md5.update(f.getBytes(Charsets.UTF_8)))
      Database.packageDependencies(pack).foreach(d => md5.update(d.getBytes(Charsets.UTF_8)))

      new BigInteger(md5.digest()).toString(16)
    }
  }
}
