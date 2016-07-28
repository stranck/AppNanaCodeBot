package it.TetrisReich.AppNanaCodeBot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.json.JSONObject;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

//status: 0 wait for code
//status: 1 wait for code confirmation
//status: 2 base status
//status: 3 feedback status
//status: 4 recive code mode
public class App{
	public static int offset;
	
    public static void main(String[] args) throws IOException{
        startup(args[0]);
    	TelegramBot bot = TelegramBotAdapter.build(args[0]);
        while(true){
        	boolean first = true;
        	GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates().offset(offset));
        	List<Update> updates = updatesResponse.updates();
        	for(Update update : updates) {
        	    Message message = update.message();
        	    //System.out.println(update.toString());
        	    //System.out.println(update.callbackQuery().toString());
        	    //System.out.println(update.callbackQuery().data());
        	    try{try{
        	    System.out.println(message.chat().id()+">"+message.text());}catch(NullPointerException e){}try{
        	    System.out.println(update.callbackQuery().message().chat().id()+">CallbackButton>"+
        	    		update.callbackQuery().data());
        	    }catch(NullPointerException e){}try{
        	    if(message.text().equalsIgnoreCase("/start")&&first){
        	    	//System.out.println("start");
        	    	//System.out.println(exist("prop\\"+message.chat().id().toString()));
        	    	if((exist("prop\\"+message.chat().id().toString()))==false){
        	    		System.out.println(message.chat().id().toString()+"> New user! ["
        	    				+message.chat().username()+"]");
        	    		bot.execute(new SendMessage(message.chat().id().toString(), "Heya! :D\n"
        	    				+ "Are you a new user? Yes, I think, so can you send me your app nana friend code?\n"
        	    				+ "Thanks ;)"));
        	    		FileO.writer("0", "status\\" + message.chat().id().toString());
        	    		FileO.writer("1", "index\\" + message.chat().id().toString());
        	    	} else FileO.writer("2", "status\\" + message.chat().id().toString());
        	    	first = false;
        	    }
        	    String[] splited = FileO.reader("status\\" + message.chat().id().toString()).split("\\s+");
        	    if(FileO.reader("status\\" + message.chat().id().toString()).equals("0")&&first) {
        	    	//int index = FileO.addWrite("list", message.text());
        	    	//FileO.writer(String.valueOf(index), "prop\\"+message.chat().id().toString());
        	    	FileO.writer(message.text(), "prop\\"+message.chat().id().toString());
        	    	System.out.println(message.chat().id()+"> Attempt to reg code.");
        	    	bot.execute(new SendMessage(message.chat().id().toString(),
        	    			"Is this ("+message.text()+") your exact AppNana code bot?"
        	    			+ "\nYou can't change it if futur.").replyMarkup(new InlineKeyboardMarkup(
        	    					new InlineKeyboardButton[]{
        	    							new InlineKeyboardButton("Yes :)").callbackData("y"),
        	    							new InlineKeyboardButton("Nop :(").callbackData("n")
        	    					})));
        	    	FileO.writer("1", "status\\" + message.chat().id().toString());
        	    	first = false;}
        	    if(message.text().equalsIgnoreCase("/github")&&first&&Integer.parseInt(splited[0])>1){
        	    	System.out.println(message.chat().id().toString()+"> Request github link.");
        	    	bot.execute(new SendMessage(message.chat().id().toString(),
        	    		"[Hope you enjoy watching how i am made :D](https://github.com/stranck/AppNanaCodeBot)")
        	    			.parseMode(ParseMode.Markdown));
        	    	FileO.writer("2", "status\\" + message.chat().id().toString());
        	    	first = false;
        	    }
        	    if(message.text().equalsIgnoreCase("/feed")&&
        	    		(message.chat().id()==175021749||message.chat().id()==50731050)&&first){
        	    	System.out.print(message.chat().id().toString()+"> Request feedback");
        	    	if(FileO.isEmpty("wait")){
        	    		String check = FileO.line("wait", 1);
        	    		bot.execute(new SendMessage(message.chat().id().toString(),
        	    				"[Feed tool]\nConferma se questo codice è valido "
        	    				+ "usando \"bad\" o \"good\""));
        	    		bot.execute(new SendMessage(message.chat().id().toString(), check));
        	    		FileO.writer("3 "+check, "status\\"+message.chat().id().toString());
        	    		System.out.println(" for code "+check);
        	    	} else { System.out.println(". No code waiting for feedback.");
        	    		bot.execute(new SendMessage(message.chat().id().toString(), "Nessun codice rimanente"));}
    	    		first = false;
        	    }
        	    if(splited[0].equals("3")&&first){
        	    	System.out.print(message.chat().id().toString()+"> Reciving feedback. ");
    	    		String id = FileO.reader("code\\"+splited[1]);
    	    		boolean good = false;
        	    	if(splited[1].equals(FileO.line("wait", 1))){
        	    		System.out.print("Code feedbacked ");
        	    		//FileO.addWrite(splited[1], "list");
        	    		//FileO.removeWrite("wait");
        	    		if(message.text().equalsIgnoreCase("bad")){
            	    		FileO.removeWrite("wait");
            	    		bot.execute(new SendMessage(id, 
            	    				"We noticed that your code is invalid. Please inserit your CORRECT code."));
            	    		FileO.writer("0", "status\\" + id);
            	    		FileO.newFile("ban\\"+id);
            	    		System.out.println("negative");
        	    		}
        	    		//String st = splited[1];
        	    		//System.out.println("-"+st+"-");
        	    		if(message.text().equalsIgnoreCase("good")){
            	    		FileO.addWrite("list", splited[1]);
            	    		FileO.removeWrite("wait");
            	    		System.out.println("positive");
            	    		good = true;
        	    		}
        	    		//System.out.println("ot");
        	    	} else System.out.println("Code already feedbacked");
        	    	FileO.writer("2", "status\\"+message.chat().id().toString());
        	    	bot.execute(new SendMessage(message.chat().id().toString(), "Done!"));
    	    		first = false;
	    			if(good)FileO.delater("ban\\"+id);
        	    }
        	    if(message.text().equalsIgnoreCase("/help")&&first&&Integer.parseInt(splited[0])>1){
        	    	System.out.println(message.chat().id().toString()+"> Request help");
        	    	bot.execute(new SendMessage(message.chat().id().toString(),
        	    			"For start getting the code of the users just type /code.\n"
        	        	    		+ "For view how I work type /github\n"
        	    					+ "For view the /help message... I thing you already know this.\n"
        	        	    		+ "If you wanna know my creator, #LucaStranck, watch "
        	        	    		+ "[this](https://www.youtube.com/channel/UCmMWUz0QZ7WhIBx-1Dz-IGg) ;)")
        	        	    				.parseMode(ParseMode.Markdown).disableWebPagePreview(true));
        	    	FileO.writer("2", "status\\" + message.chat().id().toString());
        	    	first = false;
        	    }
        	    if(message.text().equalsIgnoreCase("/code")&&
        	    		FileO.reader("status\\" + message.chat().id().toString()).equals("2")&&first){
        	    		if(exist("ban\\"+message.chat().id().toString())==false){
        	    		System.out.print(message.chat().id().toString()+"> Request code: ");
        	    		bot.execute(new SendMessage(message.chat().id().toString(),
        	    				"Inserit the code below in AppNana, then press the button below "
        	    				+"for get another code.\nEnjoy the points :D"));
        	    		int i = Integer.parseInt(FileO.reader("index\\"+message.chat().id().toString()));
        	    		if(i==Integer.parseInt(FileO.reader("prop\\"+message.chat().id().toString()))) i++;
        	    		//System.out.println(FileO.countLines("list")+" "+i);
        	    		if(i>FileO.countLines("list")){
        	    			System.out.println("Code finished");
        	    			bot.execute(new SendMessage(message.chat().id().toString(),
            	    				"I've finished the codes :c\nPlease retry later."));
        	    		} else {
        	    			SendResponse sr = bot.execute(new SendMessage(message.chat().id().toString(),
        	    				FileO.line("list", i)).replyMarkup(new InlineKeyboardMarkup(
        	    						new InlineKeyboardButton[]{
        	    								new InlineKeyboardButton("Next code").callbackData("next")
        	    				})));System.out.println(FileO.line("list", i)+"; "+i); i++;
        	    			FileO.writer(String.valueOf(i), "index\\"+message.chat().id().toString());
        	    			FileO.writer("4 "+sr.message().messageId(),"status\\"+message.chat().id().toString());}
        	    		} else {
        	    			System.out.println(message.chat().id().toString()+"> Forbidden: softbanned.");
        	    			bot.execute(new SendMessage(message.chat().id().toString(),
        	    					"You are softbanned until we confirm your code."));
        	    		}
        	    		first = false;
        	    }
        	    }catch(NullPointerException e){if (update.message() != null)e.printStackTrace();}try{
        	    if(FileO.reader("status\\" + update.callbackQuery().message().chat().id().toString()).equals("1")
        	    		&&first) {//System.out.println("enter");
        	    	if(update.callbackQuery().data().equals("y")){
        	    		System.out.println(update.callbackQuery().message().chat().id()+"> Code confirmed.");
        	    		FileO.writer("2", "status\\" + update.callbackQuery().message().chat().id().toString());
        	    		String code=FileO.reader("prop\\"+update.callbackQuery().message().chat().id().toString());
        	    		int n = FileO.addWrite("wait", code) + FileO.countLines("list");
        	    		FileO.writer(String.valueOf(n),
        	    				"prop\\"+update.callbackQuery().message().chat().id().toString());
        	    		FileO.writer(update.callbackQuery().message().chat().id().toString(), "code\\"+code);
        	    		bot.execute(new SendMessage(update.callbackQuery().message().chat().id().toString(),
        	    				"Thanks for your code! It will be share with other users :D\n"+
        	    		"For start getting the code of the users just type /code.\n"
        	    		+ "For view how I work type /github\n"
        	    		+ "If you wanna know my creator, #LucaStranck, watch "
        	    		+ "[this](https://www.youtube.com/channel/UCmMWUz0QZ7WhIBx-1Dz-IGg) ;)")
        	    				.parseMode(ParseMode.Markdown).disableWebPagePreview(true));

        	    	} else if(update.callbackQuery().data().equals("n")) {
        	    		System.out.println(update.callbackQuery().message().chat().id()+
        	    				"> Request for reinserit code.");
        	    		bot.execute(new SendMessage(update.callbackQuery().message().chat().id().toString(),
        	    				"Can you send me the CORRECT code?"));
        	    		FileO.writer("0", "status\\" + update.callbackQuery().message().chat().id().toString());
        	    	}
        	    	first = false;//System.out.println("out");
        	    }
        	    //System.out.println("test");
        	    String[] splited = FileO.reader("status\\"+
        	    		update.callbackQuery().message().chat().id().toString()).split("\\s+");
        	    //System.out.println(splited[0]);
        	    if(splited[0].equals("4")&&first) {
        	    	System.out.print(update.callbackQuery().message().chat().id().toString()+"> Request code: ");
        	    	if(update.callbackQuery().data().equals("next")){
        	    		int i = Integer.parseInt(FileO.reader("index\\"+
        	    				update.callbackQuery().message().chat().id().toString()));
        	    		if(i==Integer.parseInt(FileO.reader("prop\\"+
        	    				update.callbackQuery().message().chat().id().toString()))) i++;
        	    		//System.out.println(FileO.countLines("list"));
        	    		if(i>FileO.countLines("list")){
        	    			System.out.println("Code finished");
        	    			bot.execute(new EditMessageText(
        	    					update.callbackQuery().message().chat().id().toString(),
        	    					Integer.parseInt(splited[1]),
            	    				"I've finished the codes :c\nPlease retry later."));
        	    			FileO.writer("2","status\\"+update.callbackQuery().message().chat().id().toString());
        	    		} else {
							bot.execute(new EditMessageText(update.callbackQuery().message().chat().id().toString(),
        	    				Integer.parseInt(splited[1]),
        	    				FileO.line("list", i)).replyMarkup(new InlineKeyboardMarkup(
        	    						new InlineKeyboardButton[]{
        	    								new InlineKeyboardButton("Next code").callbackData("next")
        	    				})));System.out.println(FileO.line("list", i)+"; "+i); i++;
        	    			FileO.writer(String.valueOf(i),
        	    					"index\\"+update.callbackQuery().message().chat().id().toString());
        	    		first = false;
        	    	}}
        	    }
        	    //System.out.println(first);
    	    	if(first){
    	    		System.out.println(update.callbackQuery().message().chat().id()+
    	    				"> Inline button forbidden.");
    	    		bot.execute(new AnswerCallbackQuery(update.callbackQuery().id())
    	    				.text("There's a time and place for everything, but not now."));
    	    	}
        	    }catch(NullPointerException e){if (update.inlineQuery() != null)e.printStackTrace();}
        	    }catch(FileNotFoundException e){e.printStackTrace();}
        	    offset++;
        	}
        }
    }
    public static boolean exist(String path){
    	File f = new File(path);
    	if(f.exists() && !f.isDirectory()) return true;
    	return false;
    }
    public static void startup(String toke){
    	String up =
    			Download.dwn("https://api.telegram.org/bot"+toke+"/getupdates?limit=1");
    	JSONObject obj = new JSONObject(up);
    	offset = obj.getJSONArray("result").getJSONObject(0).getInt("update_id");
    	
    	/*boolean end = true;
    	TelegramBot bot = TelegramBotAdapter.build("145324455:AAEWB3BwFcvBWGQmMAHYr729OoQAn9aih5Q");
    	while(end){
    		GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates().offset(offset));
    		List<Update> updates = updatesResponse.updates();
    		boolean test = false;
    		for(Update update : updates){
    			test = true;
    			offset++;
    			Message message = update.message();
    		}
    		if(test) end = false;
    	}*/
    }
}
