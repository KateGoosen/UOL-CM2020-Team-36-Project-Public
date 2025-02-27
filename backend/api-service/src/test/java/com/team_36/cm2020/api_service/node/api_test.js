const axios = require('axios');

// Base URL of the API
const BASE_URL = 'http://localhost:8080/api/meeting'; // Change this if needed

// Sample UUIDs for testing
let meetingId;
let organizerToken;

// Sample data for creating a meeting
const newMeetingData = {
	title: 'Project Kickoff Meeting',
	description: 'Discussion on project goals and roadmap.',
	dateOptions: [
		{
			dateTimeStart: '2025-02-15T10:00:00',
			priority: 'HIGH',
		},
		{
			dateTimeStart: '2025-02-16T14:00:00',
			priority: 'LOW',
		},
	],
	duration: 60,
	participants: [
		{
			name: 'Alice Johnson',
			email: 'alice.johnson@example.com',
		},
		{
			name: 'Bob Smith',
			email: 'bob.smith@example.com',
		},
	],
	organizer: {
		name: 'Charlie Davis',
		email: 'charlie.davis@example.com',
	},
	timezone: 'America/New_York',
	votingDeadline: '2025-02-14T23:59:59',
};

// Sample data for finalizing a meeting
const finalizeMeetingData = {
	finalTimeSlot: '2025-02-14T10:00:00',
};

// Sample data for voting
const voteData = {
	userEmail: 'alice.johnson@example.com',
	timeSlotStart: '2025-02-14T10:00:00',
	timeSlotEnd: '2025-02-14T10:30:00',
};

// Function to create a meeting
async function createMeeting() {
	try {
		const response = await axios.post(`${BASE_URL}/new`, newMeetingData);
		meetingId = response.data.meetingId;
		organizerToken = response.data.organizerToken;
		console.log('✅', 'Create Meeting Response:', response.data);
	} catch (error) {
		console.error('Error creating meeting:', error.response?.data || error.message);
	}
}

// Function to get a meeting (organizer)
async function getMeetingForOrganizer() {
	try {
		const response = await axios.get(`${BASE_URL}/${meetingId}/${organizerToken}`);
		console.log('✅', 'Get Meeting for Organizer:', response.data);
	} catch (error) {
		console.error('Error fetching meeting:', error.response?.data || error.message);
	}
}

// Function to get the meeting's details from the participants perspective.
async function getMeetingForParticipants() {
	try {
		const response = await axios.get(`${BASE_URL}/participant/${meetingId}/alice.johnson@example.com`);
		console.log('✅', 'Get Meeting for Participants:', response.data);
	} catch (error) {
		console.error('Get meeting for participants API error:', error.response?.data || error.message);
	}
}

// Function to vote for time slots
async function voteForMeeting() {
	try {
		const response = await axios.post(`${BASE_URL}/${meetingId}/vote`, voteData);
		console.log('✅', 'Vote Response:', response.status);
		const votes = await axios.get(`${BASE_URL}/${meetingId}/${organizerToken}`);
		console.log('Meeting votes:', votes.data.participants);
	} catch (error) {
		console.error('Error voting:', error.response?.data || error.message);
	}
}

// Function to finalize the meeting
async function finalizeMeeting() {
	try {
		const response = await axios.put(`${BASE_URL}/${meetingId}/${organizerToken}/finalize`, finalizeMeetingData);
		console.log('✅', 'Finalize Meeting Response:', response.status, response.data);
	} catch (error) {
		console.error('Error finalizing meeting:', error.response?.data || error.message);
	}
}

// Function to get meetings for a participant
async function getMeetingsByEmail() {
	try {
		const response = await axios.get(`${BASE_URL}/alice.johnson@example.com`);
		console.log('✅', 'Meetings for Participant:', response.data);
	} catch (error) {
		console.error('Error fetching meetings:', error.response?.data || error.message);
	}
}

// Function to resend a meeting's link to the organizer.
async function resendMeetingLinkToOrganizer() {
	try {
		const response = await axios.get(`${BASE_URL}/restore_edit_link/${meetingId}`);
		console.log('✅', 'Resend Meeting Link Status:', response.status);
	} catch (error) {
		console.error('Resend meeting link API error:', error.response?.data || error.message);
	}
}

// Function to test API
async function testAPI() {
	try {
		const response = await axios.get(`${BASE_URL}/test`);
		console.log('✅', 'Test API Response:', response.status);
	} catch (error) {
		console.error('Test API error:', error.response?.data || error.message);
	}
}

// Execute API calls sequentially
async function runTests() {
	await createMeeting();
	await getMeetingForOrganizer();
	await getMeetingForParticipants();
	await voteForMeeting();
	await finalizeMeeting();
	await getMeetingsByEmail();
	await resendMeetingLinkToOrganizer();
	await testAPI();
}

runTests();
