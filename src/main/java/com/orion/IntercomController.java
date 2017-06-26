package com.orion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class IntercomController {
    private final UserRepository userRepository;
    private final PrivateMessageRepository privateMessageRepository;
    private final PostRepository postRepository;
    private final PokeRepository pokeRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public IntercomController(PrivateMessageRepository privateMessageRepository, UserRepository userRepository
            ,PostRepository postRepository, PokeRepository pokeRepository, CommentRepository commentRepository) {
        this.privateMessageRepository = privateMessageRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.pokeRepository = pokeRepository;
        this.commentRepository = commentRepository;
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("message", "Welcome to Orion's Intercom!");
        addPosts(model);
        return "logged_out";
    }

    @RequestMapping("/guest_action")
    public String guestAction(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", new User());
            model.addAttribute("message", "Sorry, something went wrong, please try again!");
            addPosts(model);
            return "logged_out";
        }
        if (user.getStatus().equals("register")) {
            if (userRepository.existsByUserName(user.getUserName())) {
                model.addAttribute("user", new User());
                model.addAttribute("message", "That userName already exists, please try another name!");
                addPosts(model);
                return "logged_out";
            } else {
                user.setStatus("please create a status");
                user.setImage("https://placeimg.com/300/400/people");
                user.setCreationDate(new Date());
                userRepository.save(user);
                model.addAttribute("user", user);
                model.addAttribute("comment", new Comment());
                model.addAttribute("post", new Post());
                model.addAttribute("blankUser", new User());
                addPosts(model);
                return "home";
            }
        } else {
            if (userRepository.existsByUserName(user.getUserName())) {
                User realUser = userRepository.findOneByUserName(user.getUserName());
                if (user.getPassword().equals(realUser.getPassword())) {
                    model.addAttribute("user", realUser);
                    model.addAttribute("comment", new Comment());
                    model.addAttribute("post", new Post());
                    model.addAttribute("blankUser", new User());
                    addPosts(model);
                    return "home";
                } else {
                    model.addAttribute("user", new User());
                    model.addAttribute("message", "Incorrect password, please try again");
                    addPosts(model);
                    return "logged_out";
                }
            } else {
                model.addAttribute("user", new User());
                model.addAttribute("message", "User name doesn't exists, please try again");
                addPosts(model);
                return "logged_out";
            }
        }
    }

    @RequestMapping("/poke/{postId}/{redirect}/{userName}")
    public String details(@PathVariable("postId") Integer postId,@PathVariable("redirect") String redirect
            ,@PathVariable("userName") String userName, Model model) {
        Poke poke = new Poke();
        poke.setPostId(postId);
        pokeRepository.save(poke);
        if (userName.equals("no_user")) {
            model.addAttribute("user", new User());
            model.addAttribute("message", "Congratulations, you have" +
                    " poked someone's post, go poke some more posts guest user!");
            addPosts(model);
        } else {
            User user = userRepository.findOneByUserName(userName);
            model.addAttribute("user", user);
            model.addAttribute("comment", new Comment());
            model.addAttribute("post", new Post());
            model.addAttribute("blankUser", new User());
            addPosts(model);
        }

        return redirect;
    }

    @RequestMapping("/build")
    public String buildHardCode(Model model) {
        buildHardCode();
        model.addAttribute("user", new User());
        model.addAttribute("message", "Welcome developer!");
        addPosts(model);
        return "logged_out";
    }

    @RequestMapping("/comment")
    public String comment(@Valid Comment comment, BindingResult bindingResult, Model model) {
        User user = userRepository.findOneByUserName(comment.getSender());
        comment.setTimeStamp(new Date());
        commentRepository.save(comment);
        model.addAttribute("user", user);
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", new Post());
        model.addAttribute("blankUser", new User());
        addPosts(model);
        return "home";
    }


    @RequestMapping("/post")
    public String post(@Valid Post post, BindingResult bindingResult, Model model) {
        User user = userRepository.findOneByUserName(post.getUserName());
        post.setTimeStamp(new Date());
        postRepository.save(post);
        model.addAttribute("user", user);
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", new Post());
        model.addAttribute("blankUser", new User());
        addPosts(model);
        return "home";
    }

    @RequestMapping("/edit_image")
    public String editImage(@Valid User user, BindingResult bindingResult, Model model){

        User fullUser = userRepository.findOneByUserName(user.getUserName());
        fullUser.setImage(user.getImage());
        userRepository.save(fullUser);
        model.addAttribute("user", fullUser);
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", new Post());
        model.addAttribute("blankUser", new User());
        addPosts(model);
        return "home";

    }

    @RequestMapping("/edit_status")
    public String editStatus(@Valid User user, BindingResult bindingResult, Model model){

        User fullUser = userRepository.findOneByUserName(user.getUserName());
        fullUser.setStatus(user.getStatus());
        userRepository.save(fullUser);
        model.addAttribute("user", fullUser);
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", new Post());
        model.addAttribute("blankUser", new User());
        addPosts(model);
        return "home";

    }

    @RequestMapping("/edit_password")
    public String editPassword(@Valid User user, BindingResult bindingResult, Model model){

        User fullUser = userRepository.findOneByUserName(user.getUserName());
        fullUser.setPassword(user.getPassword());
        userRepository.save(fullUser);
        model.addAttribute("user", fullUser);
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", new Post());
        model.addAttribute("blankUser", new User());
        addPosts(model);
        return "home";

    }

    @RequestMapping("/search")
    public String search(User user, BindingResult bindingResult, Model model) {
        User thisUser = userRepository.findOneByUserName(user.getStatus());
        model.addAttribute("user", thisUser);
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", new Post());
        model.addAttribute("blankUser", new User());
        if (userRepository.existsByUserName(user.getUserName())) {
            User viewing = userRepository.findOneByUserName(user.getUserName());
            viewing.setPassword("pass removed");
            addPosts(model, viewing.getUserName());
            model.addAttribute("viewing", viewing);
            model.addAttribute("privateMessage", new PrivateMessage());
            return "profile";
        } else {
            addPosts(model);
            return "home";
        }
    }

    @RequestMapping("/message")
    public String message(PrivateMessage privateMessage, BindingResult bindingResult, Model model) {
        privateMessageRepository.save(privateMessage);
        User thisUser = userRepository.findOneByUserName(privateMessage.getSender());
        model.addAttribute("user", thisUser);
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", new Post());
        model.addAttribute("blankUser", new User());
        User viewing = userRepository.findOneByUserName(privateMessage.getRecipient());
        viewing.setPassword("pass removed");
        addPosts(model, viewing.getUserName());
        model.addAttribute("viewing", viewing);
        model.addAttribute("privateMessage", new PrivateMessage());
        return "profile";
    }



/*             _______________________________
             /$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\
           <$$$$$$$$$$$PRIVATE_METHODS$$$$$$$$$$$>
             \$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$/
*/
    private int one = 0;
    private void buildHardCode() {
        if (one == 0) {
            one++; //do once..
            User hardUser = new User();
            Post hardPost = new Post();
            Post hardPost2 = new Post();
            Poke hardPoke = new Poke();
            Comment hardComment = new Comment();
            PrivateMessage hardPrivateMessage = new PrivateMessage();

            hardUser.setCreationDate(new Date());
            hardUser.setUserName("user");
            hardUser.setImage("http://vector.me/files/images/1/1/115728/man_face_world_label_outline_clip_art.jpg");
            hardUser.setStatus("Hard user status..");
            hardUser.setId(1);
            hardUser.setPassword("pass");

            hardPost.setId(1);
            hardPost.setBody("This is a hard coded post body. this should hopefully show up " +
                    "as a post. It should have a comment attached to it and one like." +
                    " here is another sentence to make it look like it has some content! -orion");
            hardPost.setTimeStamp(new Date());
            hardPost.setTitle("hard title");
            hardPost.setUserName("user");

            hardPost2.setId(2);
            hardPost2.setBody("this is another hard post, it will start with no likes and no comments");
            hardPost2.setTimeStamp(new Date());
            hardPost2.setTitle("hard title # 2");
            hardPost2.setUserName("user");

            hardPoke.setId(1);
            hardPoke.setPostId(1);

            hardComment.setBody("This is a comment on the hard post.");
            hardComment.setSender("user");
            hardComment.setTimeStamp(new Date());
            hardComment.setId(1);
            hardComment.setPostId(1);

            hardPrivateMessage.setBody("hello!");
            hardPrivateMessage.setSender("user");
            hardPrivateMessage.setRecipient("user");
            hardPrivateMessage.setId(1);
            hardPrivateMessage.setTimeStamp(new Date());

            userRepository.save(hardUser);
            postRepository.save(hardPost);
            postRepository.save(hardPost2);
            pokeRepository.save(hardPoke);
            commentRepository.save(hardComment);
            privateMessageRepository.save(hardPrivateMessage);
        }
    }
    private void addPosts(Model model) {
        ArrayList<Post> posts = postRepository.findAllByOrderByTimeStampDesc();
        model.addAttribute("posts", build(posts));
    }
    private void addPosts(Model model, String userName) {
        ArrayList<Post> posts = postRepository.findAllByUserNameOrderByTimeStampDesc(userName);
        model.addAttribute("posts", build(posts));
    }
    private ArrayList<FullPost> build(ArrayList<Post> posts) {
        ArrayList<FullPost> fullPosts = new ArrayList<>();
        for (Post post : posts) {
            ArrayList<Poke> pokes = pokeRepository.findAllByPostId(post.getId());
            ArrayList<Comment> comments = commentRepository.findAllByPostIdOrderByTimeStamp(post.getId());
            fullPosts.add(new FullPost(post,comments, pokes));
        }
        //for testing...
        System.out.println("the size of the full post list is: " + fullPosts.size());
        return fullPosts;
    }
}
