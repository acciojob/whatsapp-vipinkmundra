package com.driver;

import java.util.*;

public class WhatsappService {

    WhatsappRepository whatsappRepository = new WhatsappRepository();
    List<User> users = new ArrayList<>();
    List<Message> messages = new ArrayList<>();
    static int messageCount = 0;
    static int count = 1;
    public String createUser(String name,String mobileNo){
        try{
            if(whatsappRepository.getUserMobile().contains(mobileNo)){
                throw new Exception("User already exists");
            }
        }catch (Exception e){
            return e.getMessage();
        }
        HashSet<String> hs = whatsappRepository.getUserMobile();
        hs.add(mobileNo);
        whatsappRepository.setUserMobile(hs);
        User user = new User(name,mobileNo);
        users.add(user);
        return "User Created Successfully.";
    }
    public Group createGroup(List<User> users){
        if(users.size()<2){
            return null;
        }
        User admin = users.get(0);
        String groupName = "";
        if(users.size() == 2){
            groupName= users.get(1).getName();
        }else if(users.size()>2){
            groupName = "Group "+count;
            count++;
        }
        Group group = new Group(groupName,users.size());
        //setting the groupUserMap
        HashMap<Group,List<User>> groupUserMap =  whatsappRepository.getGroupUserMap();
        groupUserMap.put(group,users);
        whatsappRepository.setGroupUserMap(groupUserMap);
        //setting the adminMap
        HashMap<Group,User> adminMap = whatsappRepository.getAdminMap();
        adminMap.put(group,users.get(0));
        whatsappRepository.setAdminMap(adminMap);

        return group;
    }
    public int createMessage(String content){
        messageCount++;
        whatsappRepository.setMessageId(messageCount);
        int id = whatsappRepository.getMessageId();
        Message message = new Message(id,content);
        messages.add(message);

        return id;
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!whatsappRepository.getAdminMap().containsKey(group)){
            throw new Exception("Group does not exist");
        }
        List<User> users = whatsappRepository.getGroupUserMap().get(group);
        if(!users.contains(sender)){
            throw new Exception("You are not allowed to send message");
        }

        HashMap<Group,List<Message>> hs = whatsappRepository.getGroupMessageMap();
        List<Message> sendMessage = hs.get(group);
        sendMessage.add(message);
        hs.put(group,sendMessage);
        whatsappRepository.setGroupMessageMap(hs);
        return sendMessage.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!whatsappRepository.getAdminMap().containsKey(group)){
            throw new Exception("Group does not exist");
        }
        if(!whatsappRepository.getAdminMap().get(group).equals(user)){
            throw new Exception("Approver does not have rights");
        }

        List<User> users = whatsappRepository.getGroupUserMap().get(group);
        if(!users.contains(user)){
            throw new Exception("User is not a participant");
        }

        HashMap<Group,User> hs = whatsappRepository.getAdminMap();
        hs.put(group,user);
        whatsappRepository.setAdminMap(hs);
        return "Admin Updated Successfully.";
    }

    public int removeUser(User user){
        return 0;
    }
    public String findMessage(Date start, Date end, int K){
        return null;
    }
}
