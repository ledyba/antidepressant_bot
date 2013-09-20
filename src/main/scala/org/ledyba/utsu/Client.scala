package org.ledyba.utsu
import twitter4j._
import twitter4j.conf.ConfigurationBuilder
import scala.collection.JavaConverters._
import twitter4j.TwitterAdapter
import scala.util.Random

object Client {
	def create():Client={
		val cb = new ConfigurationBuilder;
		val conf = cb
		.setAsyncNumThreads(3)
		.setOAuthConsumerKey(Const.CONSUMER_KEY)
		.setOAuthConsumerSecret(Const.CONSUMER_SECRET)
		.setOAuthAccessToken(Const.ACCESS_TOKEN)
		.setOAuthAccessTokenSecret(Const.ACCESS_TOKEN_SECRET)
		.build();
		val tw = new AsyncTwitterFactory(conf).getInstance();
		val stream = new TwitterStreamFactory(conf).getInstance();
		return new Client(tw, stream);
	}
	val Keywords = Array[String](
			"鬱だ",
			"死にた",
			"自殺",
			"陰鬱",
			"生きていたくな",
			"鬱死",
			"欝打",
			"つらい",
			"首吊",
			"消えたい",
			"人生に絶望");
}

class Client(tw:AsyncTwitter, stream:TwitterStream) extends TwitterAdapter with StatusListener  {
	val myId = tw.getId();
	override def onStatus(status:Status) = {
		if( status.getInReplyToStatusId() > 0) {
			val scr = status.getUser().getScreenName();
			val max = 10;
			val r = new Random().nextInt(max);
			val msg = "@"+scr+" 大丈夫？抗うつ薬飲み忘れてない？";
			if(r == 0){
				System.out.println("[○ "+r+"/"+max+"] ("+ status.getId() +" by " + scr + ") " + status.getText());
				reply(msg, status);
			}else if(!(status.getText().indexOf("つらい") >= 0)){
				System.out.println("[特 "+r+"/"+max+"] ("+ status.getId() +" by " + scr + ") " + status.getText());
				reply(msg, status);
			}else{
				System.out.println("[× "+r+"/"+max+"] ("+ status.getId() +" by " + scr + ") " + status.getText());
			}
		}
	}
	override def updatedStatus(status:Status):Unit = {
		val scr = status.getUser().getScreenName();
		System.out.println("[Sent] ("+ status.getId() +" by " + scr + ") " + status.getText());
	}
	
	def reply(msg:String, replyTo:Status):Unit = {
		tw.updateStatus(new StatusUpdate(msg).inReplyToStatusId(replyTo.getId()));
	}

	def start():Unit = {
		stream.addListener(this);
		tw.addListener(this);
		stream.filter(new FilterQuery(0, null, Client.Keywords));
	}
	
	override def onDeletionNotice(sdn:StatusDeletionNotice):Unit = Unit;
	override def onTrackLimitationNotice(i:Int):Unit = Unit;
	override def onScrubGeo(lat:Long, lng:Long):Unit = Unit;
	override def onStallWarning(arg0:StallWarning):Unit = Unit;
	override def onException(excptn:Exception):Unit = {
		System.out.println("onException: "+excptn.toString());
	}
	override def onException(te:TwitterException, method:TwitterMethod):Unit = {
		System.out.println("onException: "+te.toString());
	}
}