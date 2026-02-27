package com.localvitrine.bootstrap;

import com.localvitrine.entity.Template;
import com.localvitrine.enums.ActivityType;
import com.localvitrine.repository.TemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Order
public class TemplateSeedRunner implements CommandLineRunner {

    private final TemplateRepository templateRepository;

    public TemplateSeedRunner(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public void run(String... args) {
        if (templateRepository.count() > 0) {
            return;
        }
        templateRepository.save(Template.builder()
                .name("Aurore")
                .code("aurore")
                .description("Mise en page aeree, typographie elegante, ideal pour services et creatifs.")
                .activityType(ActivityType.SERVICES)
                .previewUrl("https://picsum.photos/seed/aurore/800/500")
                .isActive(true)
                .build());
        templateRepository.save(Template.builder()
                .name("Bistro")
                .code("bistro")
                .description("Ambiance chaleureuse, photos mises en avant, parfait pour la restauration.")
                .activityType(ActivityType.RESTAURANT)
                .previewUrl("https://picsum.photos/seed/bistro/800/500")
                .isActive(true)
                .build());
        templateRepository.save(Template.builder()
                .name("Vitrine")
                .code("vitrine")
                .description("Grille produits claire, appels a l action visibles, oriente commerce.")
                .activityType(ActivityType.RETAIL)
                .previewUrl("https://picsum.photos/seed/vitrine/800/500")
                .isActive(true)
                .build());
    }
}
