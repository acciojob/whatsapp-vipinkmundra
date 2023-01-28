package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name,String mobileNo) throws Exception {
        if(userMobile.contains(mobileNo)){
            throw new Exception("User already exists");
        }
        userMobile.add(mobileNo);
        User user = new User(name,mobileNo);
        return "User Created Successfully.";
    }
    public Group createGroup(List<User> users){
        if(users.size() == 2){
            Group group = new Group(users.get(1).getName(),2);
            adminMap.put(group,users.get(0));
            groupUserMap.put(group,users);
            groupMessageMap.put(group,new ArrayList<Message>());
            return group;
        }
        this.customGroupCount = this.customGroupCount+1;
        Group group = new Group("Group "+this.customGroupCount,users.size());
        adminMap.put(group,users.get(0));
        groupUserMap.put(group,users);
        groupMessageMap.put(group,new ArrayList<Message>());
        return group;
    }

    public int createMessage(String content){
        this.messageId+=1;
        Message message = new Message(messageId,content);
        return messageId;
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!adminMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }else{
            if(!groupUserMap.get(group).contains(sender)){
                throw new Exception("You are not allowed to send message");
            }else{
                senderMap.put(message,sender);
                List<Message> list = groupMessageMap.get(group);
                list.add(message);
                groupMessageMap.put(group,list);
                return list.size();
            }
        }
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!adminMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }else{
            if(!adminMap.get(group).equals(approver)){
                throw new Exception("Approver does not have rights");
            }else{
                if(!groupUserMap.get(group).contains(user)){
                    throw new Exception("User is not a participant");
                }else{
                    adminMap.put(group,user);
                    return "Updated SuccessFully.";
                }
            }
        }
    }
    public int removeUser(User user){
        return 0;
    }
    public String findMessage(Date start, Date end, int K){
        return null;
    }
}
