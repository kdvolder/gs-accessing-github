package hello;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {

    private GitHub github;

    private ConnectionRepository connectionRepository;

    @Inject
    public HelloController(GitHub github, ConnectionRepository connectionRepository) {
        this.github = github;
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String helloGithub(Model model) {
        if (connectionRepository.findPrimaryConnection(GitHub.class) == null) {
            return "redirect:/connect/github";
        }
        
        GitHubUserProfile user = github.userOperations().getUserProfile();
        model.addAttribute(user);
        
        return "hello";
    }

    @RequestMapping(method=RequestMethod.GET, value="/logout")
    public String logout(Model model) {
        if (connectionRepository.findPrimaryConnection(GitHub.class) == null) {
            return "redirect:/connect/github";
        }
        
        GitHubUserProfile user = github.userOperations().getUserProfile();
        model.addAttribute(user);
        
        return "hello";
    }

}
