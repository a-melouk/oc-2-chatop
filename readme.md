# Setting Up Your Environment Variables

Before running the application, you need to configure your environment variables with your own credentials. Follow the steps below to set them up correctly.

## Required Environment Variables
The following environment variables must be set:

| Variable Name | Description | Example |
|--------------|-------------|---------|
| `USERNAME` | Database username | `dev_user` |
| `PASSWORD` | Database password | `your_password` |

## Setting Environment Variables

### On Linux/macOS
1. Open a terminal.
2. Run the following commands:
   ```sh
   export USERNAME=dev_user
   export PASSWORD=your_password
   ```
3. To make these variables persist across sessions, add them to your `~/.bashrc` or `~/.zshrc` file:
   ```sh
   echo "export USERNAME=dev_user" >> ~/.bashrc
   echo "export PASSWORD=your_password" >> ~/.bashrc
   source ~/.bashrc
   ```

### On Windows (Command Prompt)
1. Open Command Prompt.
2. Run the following commands:
   ```cmd
   set USERNAME=dev_user
   set PASSWORD=your_password
   ```

### On Windows (PowerShell)
1. Open PowerShell.
2. Run the following commands:
   ```powershell
   $env:USERNAME="dev_user"
   $env:PASSWORD="your_password"
   ```

### Using a `.env` File (Recommended for Development)
1. Create a `.env` file in the root directory of your project.
2. Add the following lines:
   ```ini
   USERNAME=dev_user
   PASSWORD=your_password
   ```

## Verifying Your Environment Variables
To ensure the variables are set correctly, run:
```sh
echo $USERNAME  # Linux/macOS
set USERNAME    # Windows CMD
echo $env:USERNAME  # Windows PowerShell
```

Now you're all set! Your application should be able to access the required environment variables.

