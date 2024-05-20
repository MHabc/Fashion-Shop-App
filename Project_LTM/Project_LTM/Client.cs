using System;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Drawing;
using System.Windows.Forms;

namespace Project_LTM
{
    public partial class Client : Form
    {
        private TcpClient client;
        private NetworkStream stream;

        public Client()
        {
            InitializeComponent();
        }

        private void btnConnect_Click(object sender, EventArgs e)
        {
            try
            {
                Int32 port = 8080;
                client = new TcpClient("127.0.0.1", port);
                stream = client.GetStream();
                MessageBox.Show("Connected to server successfully.", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error connecting to server: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private async void btnSend_Click(object sender, EventArgs e)
        {
            try
            {
                if (client == null || !client.Connected)
                {
                    MessageBox.Show("You must connect to the server first.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }

                string filePath = txtFilePath.Text.Trim();
                byte[] data = Encoding.ASCII.GetBytes(filePath + "\n");

                // Gửi đường dẫn tới Server
                await stream.WriteAsync(data, 0, data.Length);
                await stream.FlushAsync();

                byte[] buffer = new byte[4096];
                int bytesRead;
                MemoryStream memoryStream = new MemoryStream();

                // Nhận dữ liệu từ Server cho đến khi không còn dữ liệu
                while ((bytesRead = await stream.ReadAsync(buffer, 0, buffer.Length)) > 0)
                {
                    memoryStream.Write(buffer, 0, bytesRead);
                }

                byte[] fileData = memoryStream.ToArray();

                // Hiển thị dữ liệu tùy thuộc vào định dạng của file
                if (CheckImage(filePath))
                {
                    ShowImage(fileData);
                }
                else
                {
                    ShowText(fileData);
                }

                MessageBox.Show("File content received successfully.", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Exception: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private bool CheckImage(string filePath)
        {
            string[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
            string extension = Path.GetExtension(filePath);
            return imageExtensions.Contains(extension, StringComparer.OrdinalIgnoreCase);
        }

        private void ShowImage(byte[] imageData)
        {
            try
            {
                txtFileContent.Visible = false;
                pictureBox1.Visible = true;
                using (MemoryStream ms = new MemoryStream(imageData))
                {
                    pictureBox1.Image = Image.FromStream(ms);
                }
                pictureBox1.SizeMode = PictureBoxSizeMode.StretchImage;
            }
            catch
            {
                MessageBox.Show("Không thể hiển thị hình ảnh!", "Lỗi");
            }
        }

        private void ShowText(byte[] fileData)
        {
            try
            {
                string content = Encoding.UTF8.GetString(fileData);
                txtFileContent.Text = content;
                txtFileContent.Visible = true;
                pictureBox1.Visible = false;
            }
            catch
            {
                MessageBox.Show("Không thể hiển thị văn bản!", "Lỗi");
            }
        }
    }
}
