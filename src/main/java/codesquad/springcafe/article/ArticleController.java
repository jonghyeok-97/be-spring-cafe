package codesquad.springcafe.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Controller
public class ArticleController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final IArticleRepository iArticleRepository;

    public ArticleController(IArticleRepository iArticleRepository) {
        this.iArticleRepository = iArticleRepository;
    }

    // 아티클 id를 가지고 해당 아티클 보여주기 없으면 기본 홈페이지 보여주기
    @GetMapping("/article/{id}")
    public String showArticle(@PathVariable int id, Model model) {
        Optional<Article> optArticle = iArticleRepository.findBy(id);
        return optArticle.map(article -> {
            model.addAttribute("article", article);
            return "qna/show";
        }).orElse("index");
    }


    // 아티클 등록
    @PostMapping("/article")
    public String storeArticle(@ModelAttribute ArticleDto articleDto) {
        final String writer = articleDto.getWriter();
        final String title = articleDto.getTitle();
        final String contents = articleDto.getContents();
        final LocalDateTime createAt = LocalDateTime.now();
        Article article = new Article(writer, title, contents, createAt);

        log.debug("들어온 게시글 : {}", article);
        iArticleRepository.save(article);
        return "redirect:/articles";
    }

    // 모든 아티클 보여주기
    @GetMapping("/articles")
    public String showArticle(Model model) {
        Collection<Article> allArticles = iArticleRepository.findAll();
        model.addAttribute("articles", allArticles);
        return "index";
    }
}
