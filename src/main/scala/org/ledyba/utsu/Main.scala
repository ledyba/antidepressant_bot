package org.ledyba.utsu

import twitter4j._
import twitter4j.conf.ConfigurationBuilder
import scala.collection.JavaConverters._



object Main {
	def main(args: Array[String]): Unit = {
		println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		println(" 抗うつ薬飲み忘れてるよ君 ");
		println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		
		Client.create().start
	}
}