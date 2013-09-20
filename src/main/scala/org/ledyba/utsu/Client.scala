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
	val Keywords = Array[String]("鬱だ","死にた","自殺", "陰鬱", "生きていたくな", "鬱死", "欝打", "つらい");
}

class Client(tw:AsyncTwitter, stream:TwitterStream) extends TwitterAdapter with StatusListener  {
	
	override def onStatus(status:Status) = {
		val scr = status.getUser().getScreenName();
		System.out.println("Status:("+ status.getId() +") by " + scr + " " + status.getText());
		val r = new Random().nextInt(1);
		if(r == 0){
			val msg = "@"+scr+" 大丈夫？抗うつ薬飲み忘れてない？";
			println("送信: "+msg);
			reply(msg, status);
		}else{
			println("ランダムに引っかからなかった: "+r);
		}
	}
	override def updatedStatus(status:Status):Unit = {
		println("Updated: "+status.toString());
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