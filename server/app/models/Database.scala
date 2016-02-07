package models

import GlobalModules.dbConfig.driver.api._
import slick.jdbc.meta.MTable

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object Database {
  case class User(id: Option[Int], name: String)
  case class Repository(id: Option[Int], name: String, owner: Int)
  case class Permission(id: Option[Int], user: Option[Int], repository: Option[Int], admin: Boolean)
  case class Package(id: Option[Int], repo: Option[Int], name: String)
  case class Branch(id: Option[Int], packagee: Option[Int], snapshot: Boolean, name: String)
  case class File(id: Option[Int], references: Int, name: String, data: String)
  case class BranchFile(branch: Option[Int], file: Option[Int])

  object users extends TableQuery(new Users(_))
  object repositories extends TableQuery(new Repositories(_))
  object permissions extends TableQuery(new Permissions(_))
  object packages extends TableQuery(new Packages(_))
  object branches extends TableQuery(new Branches(_))
  object files extends TableQuery(new Files(_))

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.Length(32))
    def * = (id, name) <> (User.tupled, User.unapply)

    def idx = index("userNames", name, unique = true)

    def repos = users
      .join(permissions).on(_.id === _.userID)
      .join(repositories).on(_._2.repositoryID === _.id)

    def isMemberOf(id: Int) = repos.filter(_._2.id === id).exists
  }

  class Repositories(tag: Tag) extends Table[Repository](tag, "repositories") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.Length(32))
    def owner = column[Int]("owner")
    def * = (id, name, owner) <> (Repository.tupled, Repository.unapply)

    def idx = index("repositoryNames", name, unique = true)

    def members = repositories
      .join(permissions).on(_.id === _.repositoryID)
      .join(users).on(_._2.userID === _.id)
  }

  class Permissions(tag: Tag) extends Table[Permission](tag, "permissions") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def userID = column[Option[Int]]("user")
    def repositoryID = column[Option[Int]]("repository")
    def admin = column[Boolean]("admin")
    def * = (id, userID, repositoryID, admin) <> (Permission.tupled, Permission.unapply)

    def idx = index("permissions", (userID, repositoryID), unique = true)

    def user = foreignKey("userPermission", userID, users)(_.id)
    def repository = foreignKey("repositoryPermission", repositoryID, repositories)(_.id)
  }

  class Packages(tag: Tag) extends Table[Package](tag, "packages") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def repositoryID = column[Option[Int]]("repository")
    def name = column[String]("name")
    def * = (id, repositoryID, name) <> (Package.tupled, Package.unapply)

    def repository = foreignKey("repositoryPackage", repositoryID, repositories)(_.id)
  }

  class Branches(tag: Tag) extends Table[Branch](tag, "branches") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def packageID = column[Option[Int]]("package")
    def snapshot = column[Boolean]("snapshot")
    def name = column[String]("name")
    def * = (id, packageID, snapshot, name) <> (Branch.tupled, Branch.unapply)

    def packagee = foreignKey("packageBranch", packageID, packages)(_.id)
  }

  class BranchFiles(tag: Tag) extends Table[BranchFile](tag, "branchfiles") {
    def branchID = column[Option[Int]]("branch")
    def fileID = column[Option[Int]]("file")
    def * = (branchID, fileID) <> (BranchFile.tupled, BranchFile.unapply)

    def branch = foreignKey("branchBF", branchID, branches)(_.id)
    def file = foreignKey("fileBF", fileID, files)(_.id)
  }

  class Files(tag: Tag) extends Table[File](tag, "files") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def references = column[Int]("references")
    def name = column[String]("name")
    def data = column[String]("data")
    def * = (id, references, name, data) <> (File.tupled, File.unapply)
  }

  val tables = Seq(users, repositories, permissions, packages, branches, files)
  val schema = users.schema ++
    repositories.schema ++
    permissions.schema ++
    packages.schema ++
    branches.schema ++
    files.schema

  val db = GlobalModules.dbConfig.db

  def initialize(): Unit = {
    tables.foreach(table =>
      Await.result(db.run(DBIO.seq(
        MTable.getTables map (alltables => {
          if (!alltables.exists(_.name.name == table.baseTableRow.tableName)) {
            db.run(table.schema.create).onFailure{
              case t: Throwable => {
                System.err.println("Error creating table " + table.baseTableRow.tableName)
                t.printStackTrace()
              }
            }
          }
        })
      )), Duration.Inf)
    )
  }
}
