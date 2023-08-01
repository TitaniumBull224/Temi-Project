package com.ibsystem.temiassistant.domain.model

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

//@JsonClass(generateAdapter = true)
//data class WikiQuery(
//    @Json(name = "content_urls")
//    val contentUrls: ContentUrls?,
//    @Json(name = "description")
//    val description: String?,
//    @Json(name = "description_source")
//    val descriptionSource: String?,
//    @Json(name = "dir")
//    val dir: String?,
//    @Json(name = "displaytitle")
//    val displaytitle: String?,
//    @Json(name = "extract")
//    val extract: String?,
//    @Json(name = "extract_html")
//    val extractHtml: String?,
//    @Json(name = "lang")
//    val lang: String?,
//    @Json(name = "namespace")
//    val namespace: Namespace?,
//    @Json(name = "pageid")
//    val pageid: Int?,
//    @Json(name = "revision")
//    val revision: String?,
//    @Json(name = "tid")
//    val tid: String?,
//    @Json(name = "timestamp")
//    val timestamp: String?,
//    @Json(name = "title")
//    val title: String?,
//    @Json(name = "titles")
//    val titles: Titles?,
//    @Json(name = "type")
//    val type: String?,
//    @Json(name = "wikibase_item")
//    val wikibaseItem: String?
//)
//
//@JsonClass(generateAdapter = true)
//data class ContentUrls(
//    @Json(name = "desktop")
//    val desktop: Desktop?,
//    @Json(name = "mobile")
//    val mobile: Mobile?
//)
//
//@JsonClass(generateAdapter = true)
//data class Namespace(
//    @Json(name = "id")
//    val id: Int?,
//    @Json(name = "text")
//    val text: String?
//)
//
//@JsonClass(generateAdapter = true)
//data class Titles(
//    @Json(name = "canonical")
//    val canonical: String?,
//    @Json(name = "display")
//    val display: String?,
//    @Json(name = "normalized")
//    val normalized: String?
//)
//
//@JsonClass(generateAdapter = true)
//data class Desktop(
//    @Json(name = "edit")
//    val edit: String?,
//    @Json(name = "page")
//    val page: String?,
//    @Json(name = "revisions")
//    val revisions: String?,
//    @Json(name = "talk")
//    val talk: String?
//)
//
//@JsonClass(generateAdapter = true)
//data class Mobile(
//    @Json(name = "edit")@JsonClass(generateAdapter = true)
data class WikiQuery(
    @Json(name = "content_urls")
    val contentUrls: ContentUrls?,
    @Json(name = "coordinates")
    val coordinates: Coordinates?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "description_source")
    val descriptionSource: String?,
    @Json(name = "dir")
    val dir: String?,
    @Json(name = "displaytitle")
    val displaytitle: String?,
    @Json(name = "extract")
    val extract: String?,
    @Json(name = "extract_html")
    val extractHtml: String?,
    @Json(name = "lang")
    val lang: String?,
    @Json(name = "namespace")
    val namespace: Namespace?,
    @Json(name = "originalimage")
    val originalimage: Originalimage?,
    @Json(name = "pageid")
    val pageid: Int?,
    @Json(name = "revision")
    val revision: String?,
    @Json(name = "thumbnail")
    val thumbnail: Thumbnail?,
    @Json(name = "tid")
    val tid: String?,
    @Json(name = "timestamp")
    val timestamp: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "titles")
    val titles: Titles?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "wikibase_item")
    val wikibaseItem: String?
)

@JsonClass(generateAdapter = true)
data class ContentUrls(
    @Json(name = "desktop")
    val desktop: Desktop?,
    @Json(name = "mobile")
    val mobile: Mobile?
)

@JsonClass(generateAdapter = true)
data class Coordinates(
    @Json(name = "lat")
    val lat: Double?,
    @Json(name = "lon")
    val lon: Double?
)

@JsonClass(generateAdapter = true)
data class Namespace(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "text")
    val text: String?
)

@JsonClass(generateAdapter = true)
data class Originalimage(
    @Json(name = "height")
    val height: Int?,
    @Json(name = "source")
    val source: String?,
    @Json(name = "width")
    val width: Int?
)

@JsonClass(generateAdapter = true)
data class Thumbnail(
    @Json(name = "height")
    val height: Int?,
    @Json(name = "source")
    val source: String?,
    @Json(name = "width")
    val width: Int?
)

@JsonClass(generateAdapter = true)
data class Titles(
    @Json(name = "canonical")
    val canonical: String?,
    @Json(name = "display")
    val display: String?,
    @Json(name = "normalized")
    val normalized: String?
)

@JsonClass(generateAdapter = true)
data class Desktop(
    @Json(name = "edit")
    val edit: String?,
    @Json(name = "page")
    val page: String?,
    @Json(name = "revisions")
    val revisions: String?,
    @Json(name = "talk")
    val talk: String?
)

@JsonClass(generateAdapter = true)
data class Mobile(
    @Json(name = "edit")
    val edit: String?,
    @Json(name = "page")
    val page: String?,
    @Json(name = "revisions")
    val revisions: String?,
    @Json(name = "talk")
    val talk: String?
)
//    val edit: String?,
//    @Json(name = "page")
//    val page: String?,
//    @Json(name = "revisions")
//    val revisions: String?,
//    @Json(name = "talk")
//    val talk: String?
//)

