package io.github.trainb0y.classroom

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.classroom.Classroom
import java.io.File

object Google {
	val json = GsonFactory.getDefaultInstance()
	val transport = GoogleNetHttpTransport.newTrustedTransport()
	val scopes = listOf(
		"https://www.googleapis.com/auth/classroom.courses.readonly",
		"https://www.googleapis.com/auth/classroom.coursework.me.readonly",
		"https://www.googleapis.com/auth/classroom.topics.readonly",
		"https://www.googleapis.com/auth/classroom.announcements.readonly",
		"https://www.googleapis.com/auth/classroom.courseworkmaterials.readonly",
	)
	fun main() {
		val service = Classroom.Builder(transport, json, credentials(transport)).build()

		val courses = service.courses().list().execute().courses.forEach { println(it) }
	}

	fun credentials(transport: NetHttpTransport): Credential {
		val secrets = GoogleClientSecrets.load(json, File("sec/credentials.json").reader())
		val flow = GoogleAuthorizationCodeFlow.Builder(transport, json, secrets, scopes)
			.setDataStoreFactory(FileDataStoreFactory(File("sec/tokens")))
			.setAccessType("offline")
			.build()

		val receiver = LocalServerReceiver.Builder().setPort(8888).build()
		return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
	}
}