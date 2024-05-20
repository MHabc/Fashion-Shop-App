using System;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Windows.Forms;

namespace Project_LTM
{
    public partial class Server : Form
    {
        private Thread serverThread;
        private Socket listenerSocket;

        public Server()
        {
            InitializeComponent();
        }

        private void btnStart_Click(object sender, EventArgs e)
        {
            serverThread = new Thread(StartListen);
            serverThread.Start();
            listView1.Items.Add("Server started!");
            LoadDrives();
        }

        private void ReceiveData(Socket clientSocket)
        {
            int bytesReceived = 0;
            byte[] receive = new byte[1024];
            string clientAddress = ((IPEndPoint)clientSocket.RemoteEndPoint).Address.ToString();
            int clientPort = ((IPEndPoint)clientSocket.RemoteEndPoint).Port;

            if (listView1.InvokeRequired)
            {
                listView1.Invoke((MethodInvoker)delegate
                {
                    listView1.Items.Add($"Connection accepted from {clientAddress}:{clientPort}");
                });
            }

            while (clientSocket.Connected)
            {
                string text = "";
                do
                {
                    bytesReceived = clientSocket.Receive(receive);
                    text += Encoding.UTF8.GetString(receive, 0, bytesReceived);
                } while (!text.EndsWith("\n"));

                if (listView1.InvokeRequired)
                {
                    listView1.Invoke((MethodInvoker)delegate
                    {
                        listView1.Items.Add($"Received: {text}");
                    });
                }

                string filePath = text.Trim();

                try
                {
                    byte[] fileData;
                    if (IsImage(filePath))
                    {
                        fileData = File.ReadAllBytes(filePath);
                    }
                    else
                    {
                        fileData = Encoding.UTF8.GetBytes(File.ReadAllText(filePath));
                    }

                    clientSocket.Send(fileData);
                    clientSocket.Shutdown(SocketShutdown.Send);

                    listView1.Invoke((MethodInvoker)delegate
                    {
                        listView1.Items.Add($"Sent: {fileData.Length} bytes");
                    });
                }
                catch (Exception ex)
                {
                    string errorMsg = "Error reading file: " + ex.Message;
                    byte[] msg = Encoding.ASCII.GetBytes(errorMsg);
                    clientSocket.Send(msg);
                    clientSocket.Shutdown(SocketShutdown.Send);
                    listView1.Invoke((MethodInvoker)delegate
                    {
                        listView1.Items.Add($"Sent: {errorMsg}");
                    });
                }
            }

            clientSocket.Close();
        }

        private void StartListen()
        {
            listenerSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            IPEndPoint iPEndPoint = new IPEndPoint(IPAddress.Any, 8080);

            listenerSocket.Bind(iPEndPoint);
            listenerSocket.Listen(5);

            while (true)
            {
                var clientSocket = listenerSocket.Accept();
                Thread thread = new Thread(() => ReceiveData(clientSocket));
                thread.Start();
            }
        }

        private bool IsImage(string filePath)
        {
            string[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
            string extension = Path.GetExtension(filePath);
            return imageExtensions.Contains(extension, StringComparer.OrdinalIgnoreCase);
        }

        private void LoadDrives()
        {
            DriveInfo[] drives = DriveInfo.GetDrives();
            foreach (DriveInfo drive in drives)
            {
                TreeNode node = new TreeNode(drive.Name);
                node.Tag = drive.RootDirectory;
                treeView1.Nodes.Add(node);
                LoadNode(node);
            }
        }

        private void LoadNode(TreeNode parentNode)
        {
            DirectoryInfo[] directories = ((DirectoryInfo)parentNode.Tag).GetDirectories();
            AddNodes(parentNode, directories);

            FileInfo[] files = ((DirectoryInfo)parentNode.Tag).GetFiles();
            AddNodes(parentNode, files);
        }

        private void AddNodes(TreeNode parentNode, FileSystemInfo[] items)
        {
            foreach (var item in items)
            {
                TreeNode node;
                if (item is DirectoryInfo directory)
                {
                    node = new TreeNode(directory.Name);
                    node.Tag = directory;
                    node.Nodes.Add("");
                }
                else
                {
                    node = new TreeNode(item.Name);
                    node.Tag = item;
                }
                parentNode.Nodes.Add(node);
            }
        }

        private void Server_FormClosing(object sender, FormClosingEventArgs e)
        {
            Environment.Exit(0);
        }
    }
}
