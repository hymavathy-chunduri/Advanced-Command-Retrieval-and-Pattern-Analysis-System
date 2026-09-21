/**
 * MongoDB Atlas Seed Script for Linux Command Intelligence
 * 
 * Instructions:
 * 1. Open MongoDB Compass or connect via mongosh:
 *    mongosh "mongodb+srv://admin:LinuxCmdPass123!@cluster0.ha3ezc1.mongodb.net/linux_command_db"
 * 
 * 2. Run this script or insert document collection.
 */

db = db.getSiblingDB('linux_command_db');

// Create collection with index
db.createCollection('linux_commands');

db.linux_commands.createIndex({ command: 1 }, { unique: true });
db.linux_commands.createIndex({ category: 1 });
db.linux_commands.createIndex({ "$**": "text" });

print("Created collection 'linux_commands' with indexes on MongoDB Atlas database 'linux_command_db'.");
