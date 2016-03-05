package models

import models.author.{Author, Dev}

/**
  * Represents an Ore package. The specified ID should correspond with the
  * actual plugin id defined in the meta file. (TODO: check meta file in file
  * uploads).
  *
  * @param id          Plugin ID
  * @param name        Name of plugin
  * @param description Short description of plugin
  * @param owner       The owner Author for this project
  * @param authors     List of addition authors on this project
  */
case class Project(id: String, name: String, description: String, owner: Author, authors: List[Author]) {

  var views = 0
  var downloads = 0
  var starred = 0

  def this(id: String, name: String, description: String, owner: Author) = this(id, name, description, owner, List(owner))

  override def toString = "%s - %s".format(name, description)

}

object Project {

  // TODO: Replace with DB
  var projects = Seq(
    new Project("example1", "Example-1", "Description 1", Dev.get("Author1").get),
    new Project("example2", "Example-2", "Description 2", Dev.get("Author2").get),
    new Project("example3", "Example-3", "Description 3", Dev.get("Author3").get),
    new Project("example4", "Example-4", "Description 4", Dev.get("Author4").get),
    new Project("example5", "Example-5", "Description 5", Dev.get("Author5").get)
  )

  def get(owner: String, name: String): Option[Project] = {
    for (project <- projects) {
      if (project.owner.name.equals(owner) && project.name.equals(name)) {
        return Some(project)
      }
    }
    None
  }

}
