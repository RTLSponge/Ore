package security.spauth

/**
  * Represents a Sponge user.
  *
  * @param id       Unique ID
  * @param username Username
  * @param email    Email
  */
case class SpongeUser(id: Int, username: String, email: String, avatarUrl: Option[String])
