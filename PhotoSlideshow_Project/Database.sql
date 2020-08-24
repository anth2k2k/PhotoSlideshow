USE [ImageSlideshow]
GO
/****** Object:  Table [dbo].[tblImg]    Script Date: 8/24/2020 4:48:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tblImg](
	[URL] [nvarchar](max) NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
