# CloudSim Simulation with CI/CD

This project simulates cloud resource scheduling, auto-scaling, and fault tolerance using CloudSim, Docker, and GitHub Actions for CI/CD integration.

## 🚀 Features

- ☁️ VM Scheduling and Load Balancing
- 🧠 Cloudlet Execution with Resource Utilization
- 📈 Auto-Scaling Simulation (Based on CPU load)
- 🛠️ Fault Tolerance (Redistribution if VM fails)
- 🔁 CI/CD Pipeline with GitHub Actions
- 🐳 Dockerized Java Environment

## 🛠️ Tech Stack

- Java + CloudSim
- Docker
- GitHub Actions

## 🧪 Run Locally

Make sure Docker is installed, then run:

```bash
docker build -t cloudsim-project .
docker run cloudsim-project
```

## 📁 Project Structure

CloudSim-Project/
├── src/
│ └── CloudSimExample.java # Main simulation logic
├── lib/ # CloudSim JAR files
├── Dockerfile # Java runtime + build steps
├── .github/
│ └── workflows/
│ └── ci.yml # CI/CD pipeline config
├── README.md
└── LICENSE

## ⚙️ GitHub Actions Workflow

.github/workflows/ci.yml performs the following steps:

- Sets up the Java environment

- Compiles your Java source code

- Builds the Docker image

## 📌 Prerequisites

- Java installed (for local testing)

- Place required CloudSim JARs in the lib/ folder

- Docker installed and running

- GitHub repository connected for CI/CD

## ✍️ Author

_Dhairya A Mehra_
CSE, Batch 2022-26
