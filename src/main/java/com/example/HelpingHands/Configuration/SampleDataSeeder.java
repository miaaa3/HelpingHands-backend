package com.example.HelpingHands.Configuration;

import com.example.HelpingHands.Entity.Comment;
import com.example.HelpingHands.Entity.Donation;
import com.example.HelpingHands.Entity.DonationStatus;
import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.Gender;
import com.example.HelpingHands.Entity.Like;
import com.example.HelpingHands.Entity.Message;
import com.example.HelpingHands.Entity.Opportunity;
import com.example.HelpingHands.Entity.OpportunityApplication;
import com.example.HelpingHands.Entity.OpportunityCategory;
import com.example.HelpingHands.Entity.OpportunityStatus;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import com.example.HelpingHands.Entity.Post;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Entity.Volunteer;
import com.example.HelpingHands.Repository.CommentRepository;
import com.example.HelpingHands.Repository.DonationRepository;
import com.example.HelpingHands.Repository.FollowRepository;
import com.example.HelpingHands.Repository.LikeRepository;
import com.example.HelpingHands.Repository.MessageRepository;
import com.example.HelpingHands.Repository.OpportunityRepository;
import com.example.HelpingHands.Repository.OrganizationRepository;
import com.example.HelpingHands.Repository.PostRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SampleDataSeeder {

    private static final String SAMPLE_PASSWORD = "Password123!";

    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final MessageRepository messageRepository;
    private final DonationRepository donationRepository;
    private final OpportunityRepository opportunityRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(name = "sample-data.enabled", havingValue = "true")
    CommandLineRunner seedSampleData() {
        return args -> {
            if (userRepository.findByEmail("maya.volunteer@helpinghands.test").isPresent()) {
                return;
            }

            Volunteer maya = volunteer("maya.volunteer@helpinghands.test", "maya", "Maya Carter",
                    "Paris, France", "+33 6 10 20 30 40", Gender.FEMALE,
                    Set.of("Food distribution", "Education", "Community events"));
            Volunteer theo = volunteer("theo.volunteer@helpinghands.test", "theo", "Theo Martin",
                    "Lyon, France", "+33 6 11 22 33 44", Gender.MALE,
                    Set.of("Environment", "Animal care", "Logistics"));
            Volunteer lina = volunteer("lina.volunteer@helpinghands.test", "lina", "Lina Haddad",
                    "Marseille, France", "+33 6 55 44 33 22", Gender.FEMALE,
                    Set.of("Health", "Tutoring", "Translation"));

            Organization foodBridge = organization("foodbridge.org@helpinghands.test", "foodbridge", "FoodBridge Paris",
                    "Paris, France", "+33 1 44 20 10 10",
                    "FoodBridge rescues surplus meals and distributes them to families, students, and seniors.",
                    "Food aid", "Amelie Laurent");
            Organization greenSteps = organization("greensteps.org@helpinghands.test", "greensteps", "GreenSteps",
                    "Lyon, France", "+33 4 72 00 10 10",
                    "GreenSteps organizes neighborhood cleanups, urban garden days, and recycling workshops.",
                    "Environment", "Noah Bernard");
            Organization brightFuture = organization("brightfuture.org@helpinghands.test", "brightfuture", "Bright Future Tutors",
                    "Remote / Marseille", "+33 4 91 00 20 20",
                    "Bright Future Tutors connects volunteer mentors with students who need free homework support.",
                    "Education", "Sarah Benali");

            // Platform admin account - manages the admin panel (verification, moderation, user accounts).
            UserEntity admin = new UserEntity("admin@helpinghands.test", passwordEncoder.encode(SAMPLE_PASSWORD),
                    "Paris, France", "+33 1 00 00 00 00", "ADMIN", "admin");
            admin.setProfile("https://ui-avatars.com/api/?name=Admin&background=374151&color=fff");
            userRepository.save(admin);

            // Demo data: a couple of orgs already verified, one still pending review.
            foodBridge.setVerificationStatus(OrganizationVerificationStatus.VERIFIED);
            greenSteps.setVerificationStatus(OrganizationVerificationStatus.VERIFIED);

            volunteerRepository.saveAll(List.of(maya, theo, lina));
            organizationRepository.saveAll(List.of(foodBridge, greenSteps, brightFuture));

            createFollows(List.of(maya, theo, lina, foodBridge, greenSteps, brightFuture));
            createPosts(maya, theo, lina, foodBridge, greenSteps, brightFuture);
            createOpportunities(maya, theo, lina, foodBridge, greenSteps, brightFuture);
            createMessages(maya, theo, foodBridge, greenSteps, brightFuture);
            createDonations(maya, theo, lina, foodBridge, greenSteps, brightFuture);
        };
    }

    private Volunteer volunteer(String email, String name, String fullName, String address, String phone,
                                Gender gender, Set<String> interests) {
        Volunteer volunteer = new Volunteer(email, passwordEncoder.encode(SAMPLE_PASSWORD), address, phone,
                "VOLUNTEER", name, fullName, gender, new Date());
        volunteer.setInterests(interests);
        volunteer.setProfile("https://ui-avatars.com/api/?name=" + fullName.replace(" ", "+") + "&background=2f855a&color=fff");
        return volunteer;
    }

    private Organization organization(String email, String name, String displayName, String address, String phone,
                                      String description, String type, String founder) {
        Organization organization = new Organization(email, passwordEncoder.encode(SAMPLE_PASSWORD), address, phone,
                "ORGANIZATION", name, description, type, founder, new Date());
        organization.setWebsite("https://" + name + ".example");
        organization.setProfile("https://ui-avatars.com/api/?name=" + displayName.replace(" ", "+") + "&background=2563eb&color=fff");
        return organization;
    }

    private void createFollows(List<UserEntity> users) {
        for (UserEntity user : users) {
            followRepository.save(new Follow(user, user));
        }

        followRepository.saveAll(List.of(
                new Follow(users.get(0), users.get(3)),
                new Follow(users.get(0), users.get(4)),
                new Follow(users.get(1), users.get(3)),
                new Follow(users.get(1), users.get(4)),
                new Follow(users.get(2), users.get(3)),
                new Follow(users.get(2), users.get(5)),
                new Follow(users.get(3), users.get(0)),
                new Follow(users.get(4), users.get(1)),
                new Follow(users.get(5), users.get(2))
        ));
    }

    private void createPosts(Volunteer maya, Volunteer theo, Volunteer lina,
                             Organization foodBridge, Organization greenSteps, Organization brightFuture) {
        Post p1 = postRepository.save(new Post("We packed 120 grocery kits tonight. Huge thanks to everyone who helped after work.", foodBridge));
        Post p2 = postRepository.save(new Post("Saturday cleanup route is ready. Bring gloves if you have them, we will provide bags and water.", greenSteps));
        Post p3 = postRepository.save(new Post("First tutoring session went great. Two students asked for extra math practice next week.", brightFuture));
        Post p4 = postRepository.save(new Post("I am free this weekend for food distribution or event setup near Paris.", maya));
        Post p5 = postRepository.save(new Post("Looking for more environmental volunteering days around Lyon. Happy to help with logistics.", theo));

        Comment c1 = commentRepository.save(comment("Count me in for the next packing night.", maya, p1));
        Comment c2 = commentRepository.save(comment("I can bring two friends to the cleanup.", theo, p2));
        Comment c3 = commentRepository.save(comment("Remote tutoring is perfect for my schedule.", lina, p3));
        Comment c4 = commentRepository.save(comment("Thank you, Maya. We will message you the details.", foodBridge, p4));

        likeRepository.saveAll(List.of(
                like(maya, p1), like(theo, p1), like(lina, p1),
                like(maya, p2), like(lina, p2),
                like(foodBridge, p4), like(greenSteps, p5), like(brightFuture, p3)
        ));
    }

    private Comment comment(String content, UserEntity user, Post post) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);
        return comment;
    }

    private Like like(UserEntity user, Post post) {
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        like.setIsLiked(true);
        return like;
    }

    private void createOpportunities(Volunteer maya, Volunteer theo, Volunteer lina,
                                     Organization foodBridge, Organization greenSteps, Organization brightFuture) {
        Opportunity pantry = new Opportunity(
                "Evening Pantry Packing",
                "Sort donated groceries and prepare family food kits for the weekend distribution.",
                OpportunityCategory.COMMUNITY,
                LocalDateTime.now().plusDays(4).withHour(18).withMinute(30),
                "Paris 11e Community Center",
                12,
                OpportunityStatus.OPEN
        );
        pantry.setOrganization(foodBridge);
        pantry.addApplication(new OpportunityApplication(pantry, maya));
        pantry.addApplication(new OpportunityApplication(pantry, theo));

        Opportunity cleanup = new Opportunity(
                "Riverbank Cleanup Morning",
                "Help remove litter, separate recyclables, and map areas that need city pickup support.",
                OpportunityCategory.ENVIRONMENT,
                LocalDateTime.now().plusDays(9).withHour(9).withMinute(0),
                "Lyon, Parc de la Tete d'Or",
                20,
                OpportunityStatus.OPEN
        );
        cleanup.setOrganization(greenSteps);
        cleanup.addApplication(new OpportunityApplication(cleanup, theo));

        Opportunity tutoring = new Opportunity(
                "Remote Homework Mentoring",
                "Support middle-school students with math, English, and study planning over video call.",
                OpportunityCategory.EDUCATION,
                LocalDateTime.now().plusDays(2).withHour(17).withMinute(0),
                "Remote",
                8,
                OpportunityStatus.OPEN
        );
        tutoring.setOrganization(brightFuture);
        tutoring.addApplication(new OpportunityApplication(tutoring, lina));
        tutoring.addApplication(new OpportunityApplication(tutoring, maya));

        Opportunity firstAid = new Opportunity(
                "Community First Aid Workshop Support",
                "Welcome attendees, prepare materials, and help instructors during a public health workshop.",
                OpportunityCategory.HEALTH,
                LocalDateTime.now().plusDays(14).withHour(10).withMinute(0),
                "Marseille Community Hall",
                6,
                OpportunityStatus.OPEN
        );
        firstAid.setOrganization(brightFuture);

        opportunityRepository.saveAll(List.of(pantry, cleanup, tutoring, firstAid));
    }

    private void createMessages(Volunteer maya, Volunteer theo,
                                Organization foodBridge, Organization greenSteps, Organization brightFuture) {
        messageRepository.saveAll(List.of(
                message(maya, foodBridge, "Hi FoodBridge, I can help with packing this week if you still need volunteers.", true),
                message(foodBridge, maya, "Yes please. Thursday at 18:30 would be perfect.", false),
                message(theo, greenSteps, "Do you need someone to coordinate supplies for the cleanup?", true),
                message(greenSteps, theo, "That would help a lot. We need bags, gloves, and two check-in volunteers.", false),
                message(brightFuture, maya, "Thanks for applying to mentor. Are Tuesdays good for you?", false)
        ));
    }

    private Message message(UserEntity sender, UserEntity receiver, String content, boolean isRead) {
        return Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .isRead(isRead)
                .build();
    }

    private void createDonations(Volunteer maya, Volunteer theo, Volunteer lina,
                                 Organization foodBridge, Organization greenSteps, Organization brightFuture) {
        donationRepository.saveAll(List.of(
                donation(maya, foodBridge, "35.00", "pi_seed_foodbridge_maya", "For tonight's grocery kits.", false),
                donation(theo, greenSteps, "20.00", "pi_seed_greensteps_theo", "For cleanup supplies.", false),
                donation(lina, brightFuture, "50.00", "pi_seed_brightfuture_lina", "For student learning materials.", false),
                donation(maya, brightFuture, "15.00", "pi_seed_brightfuture_maya", "Small boost for tutoring.", true)
        ));
    }

    private Donation donation(UserEntity donor, Organization organization, String amount,
                              String paymentIntentId, String message, boolean anonymous) {
        return Donation.builder()
                .donor(donor)
                .organization(organization)
                .amount(new BigDecimal(amount))
                .currency("EUR")
                .status(DonationStatus.SUCCEEDED)
                .stripePaymentIntentId(paymentIntentId)
                .message(message)
                .anonymous(anonymous)
                .build();
    }
}
