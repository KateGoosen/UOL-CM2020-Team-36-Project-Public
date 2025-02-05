package com.team_36.cm2020.notifications_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Define Exchange Names
    private static final String MEETING_EXCHANGE = "meeting.exchange";
    private static final String VOTE_EXCHANGE = "vote.exchange";
    private static final String AUTH_EXCHANGE = "auth.exchange";

    // Define Queue Names
    private static final String MEETING_CREATED_ORGANIZER = "meeting_created_organizer";
    private static final String MEETING_CREATED_PARTICIPANTS = "meeting_created_participants";
    private static final String VOTE_REGISTERED_ORGANIZER = "vote_registered_organizer";
    private static final String VOTE_REGISTERED_PARTICIPANT = "vote_registered_participant";
    private static final String MEETING_FINALIZED_ORGANIZER = "meeting_finalized_organizer";
    private static final String MEETING_NO_SLOTS_ORGANIZER = "meeting_no_slots_organizer";
    private static final String MEETING_NO_SLOTS_PARTICIPANTS = "meeting_no_slots_participants";
    private static final String MEETING_FINALIZED_PARTICIPANTS = "meeting_finalized_participants";
    private static final String MEETING_DELETED_ORGANIZER = "meeting_deleted_organizer";
    private static final String AUTH_SIGNUP_CONFIRMATION_CODE = "auth_signup_confirmation_code";
    private static final String AUTH_SIGNUP_SUCCESS = "auth_signup_success";
    private static final String AUTH_LOGIN_CONFIRMATION_CODE = "auth_login_confirmation_code";
    private static final String AUTH_PASSWORD_RESET = "auth_password_reset";

    // Define Routing Keys
    private static final String MEETING_CREATED_ORGANIZER_KEY = "meeting.created.organizer";
    private static final String MEETING_CREATED_PARTICIPANTS_KEY = "meeting.created.participants";
    private static final String VOTE_REGISTERED_ORGANIZER_KEY = "vote.registered.organizer";
    private static final String VOTE_REGISTERED_PARTICIPANT_KEY = "vote.registered.participant";
    private static final String MEETING_FINALIZED_ORGANIZER_KEY = "meeting.finalized.organizer";
    private static final String MEETING_NO_SLOTS_ORGANIZER_KEY = "meeting.no_slots.organizer";
    private static final String MEETING_NO_SLOTS_PARTICIPANTS_KEY = "meeting.no_slots.participants";
    private static final String MEETING_FINALIZED_PARTICIPANTS_KEY = "meeting.finalized.participants";
    private static final String MEETING_DELETED_ORGANIZER_KEY = "meeting.deleted.organizer";
    private static final String AUTH_SIGNUP_CONFIRMATION_CODE_KEY = "auth.signup.confirmation_code";
    private static final String AUTH_SIGNUP_SUCCESS_KEY = "auth.signup.success";
    private static final String AUTH_LOGIN_CONFIRMATION_CODE_KEY = "auth.login.confirmation_code";
    private static final String AUTH_PASSWORD_RESET_KEY = "auth.password.reset";

    @Autowired
    private AmqpAdmin amqpAdmin;

    // Define Exchanges
    @Bean
    public DirectExchange meetingExchange() {
        return new DirectExchange(MEETING_EXCHANGE);
    }

    @Bean
    public DirectExchange voteExchange() {
        return new DirectExchange(VOTE_EXCHANGE);
    }

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange(AUTH_EXCHANGE);
    }

    // Define Queues
    @Bean
    public Queue meetingCreatedOrganizerQueue() {
        return new Queue(MEETING_CREATED_ORGANIZER, true);
    }

    @Bean
    public Queue meetingCreatedParticipantsQueue() {
        return new Queue(MEETING_CREATED_PARTICIPANTS, true);
    }

    @Bean
    public Queue voteRegisteredOrganizerQueue() {
        return new Queue(VOTE_REGISTERED_ORGANIZER, true);
    }

    @Bean
    public Queue voteRegisteredParticipantQueue() {
        return new Queue(VOTE_REGISTERED_PARTICIPANT, true);
    }

    @Bean
    public Queue meetingFinalizedOrganizerQueue() {
        return new Queue(MEETING_FINALIZED_ORGANIZER, true);
    }

    @Bean
    public Queue meetingNoSlotsOrganizerQueue() {
        return new Queue(MEETING_NO_SLOTS_ORGANIZER, true);
    }

    @Bean
    public Queue meetingNoSlotsParticipantsQueue() {
        return new Queue(MEETING_NO_SLOTS_PARTICIPANTS, true);
    }

    @Bean
    public Queue meetingFinalizedParticipantsQueue() {
        return new Queue(MEETING_FINALIZED_PARTICIPANTS, true);
    }

    @Bean
    public Queue meetingDeletedOrganizerQueue() {
        return new Queue(MEETING_DELETED_ORGANIZER, true);
    }

    @Bean
    public Queue authSignupConfirmationCodeQueue() {
        return new Queue(AUTH_SIGNUP_CONFIRMATION_CODE, true);
    }

    @Bean
    public Queue authSignupSuccessQueue() {
        return new Queue(AUTH_SIGNUP_SUCCESS, true);
    }

    @Bean
    public Queue authLoginConfirmationCodeQueue() {
        return new Queue(AUTH_LOGIN_CONFIRMATION_CODE, true);
    }

    @Bean
    public Queue authPasswordResetQueue() {
        return new Queue(AUTH_PASSWORD_RESET, true);
    }

    // Bindings (Queue to Exchange)
    @Bean
    public Binding bindMeetingCreatedOrganizer() {
        return BindingBuilder.bind(meetingCreatedOrganizerQueue()).to(meetingExchange()).with(MEETING_CREATED_ORGANIZER_KEY);
    }

    @Bean
    public Binding bindMeetingCreatedParticipants() {
        return BindingBuilder.bind(meetingCreatedParticipantsQueue()).to(meetingExchange()).with(MEETING_CREATED_PARTICIPANTS_KEY);
    }

    @Bean
    public Binding bindVoteRegisteredOrganizer() {
        return BindingBuilder.bind(voteRegisteredOrganizerQueue()).to(voteExchange()).with(VOTE_REGISTERED_ORGANIZER_KEY);
    }

    @Bean
    public Binding bindVoteRegisteredParticipant() {
        return BindingBuilder.bind(voteRegisteredParticipantQueue()).to(voteExchange()).with(VOTE_REGISTERED_PARTICIPANT_KEY);
    }

    @Bean
    public Binding bindMeetingFinalizedOrganizer() {
        return BindingBuilder.bind(meetingFinalizedOrganizerQueue()).to(meetingExchange()).with(MEETING_FINALIZED_ORGANIZER_KEY);
    }

    @Bean
    public Binding bindMeetingNoSlotsOrganizer() {
        return BindingBuilder.bind(meetingNoSlotsOrganizerQueue()).to(meetingExchange()).with(MEETING_NO_SLOTS_ORGANIZER_KEY);
    }

    @Bean
    public Binding bindMeetingNoSlotsParticipants() {
        return BindingBuilder.bind(meetingNoSlotsParticipantsQueue()).to(meetingExchange()).with(MEETING_NO_SLOTS_PARTICIPANTS_KEY);
    }

    @Bean
    public Binding bindMeetingFinalizedParticipants() {
        return BindingBuilder.bind(meetingFinalizedParticipantsQueue()).to(meetingExchange()).with(MEETING_FINALIZED_PARTICIPANTS_KEY);
    }

    @Bean
    public Binding bindMeetingDeletedOrganizer() {
        return BindingBuilder.bind(meetingDeletedOrganizerQueue()).to(meetingExchange()).with(MEETING_DELETED_ORGANIZER_KEY);
    }

    @Bean
    public Binding bindAuthSignupConfirmationCode() {
        return BindingBuilder.bind(authSignupConfirmationCodeQueue()).to(authExchange()).with(AUTH_SIGNUP_CONFIRMATION_CODE_KEY);
    }

    @Bean
    public Binding bindAuthSignupSuccess() {
        return BindingBuilder.bind(authSignupSuccessQueue()).to(authExchange()).with(AUTH_SIGNUP_SUCCESS_KEY);
    }

    @Bean
    public Binding bindAuthLoginConfirmationCode() {
        return BindingBuilder.bind(authLoginConfirmationCodeQueue()).to(authExchange()).with(AUTH_LOGIN_CONFIRMATION_CODE_KEY);
    }

    @Bean
    public Binding bindAuthPasswordReset() {
        return BindingBuilder.bind(authPasswordResetQueue()).to(authExchange()).with(AUTH_PASSWORD_RESET_KEY);
    }

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            amqpAdmin.declareExchange(meetingExchange());
            amqpAdmin.declareExchange(voteExchange());
            amqpAdmin.declareExchange(authExchange());

            amqpAdmin.declareQueue(meetingCreatedOrganizerQueue());
            amqpAdmin.declareQueue(meetingCreatedParticipantsQueue());
            amqpAdmin.declareQueue(voteRegisteredOrganizerQueue());
            amqpAdmin.declareQueue(voteRegisteredParticipantQueue());
            amqpAdmin.declareQueue(meetingFinalizedOrganizerQueue());
            amqpAdmin.declareQueue(meetingNoSlotsOrganizerQueue());
            amqpAdmin.declareQueue(meetingNoSlotsParticipantsQueue());
            amqpAdmin.declareQueue(meetingFinalizedParticipantsQueue());
            amqpAdmin.declareQueue(meetingDeletedOrganizerQueue());
            amqpAdmin.declareQueue(authSignupConfirmationCodeQueue());
            amqpAdmin.declareQueue(authSignupSuccessQueue());
            amqpAdmin.declareQueue(authLoginConfirmationCodeQueue());
            amqpAdmin.declareQueue(authPasswordResetQueue());

            System.out.println("RabbitMQ: All exchanges and queues have been initialized.");
        };
    }
}

