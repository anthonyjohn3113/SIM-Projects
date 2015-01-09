-- phpMyAdmin SQL Dump
-- version 3.5.2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 16, 2012 at 06:34 PM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `iab`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE IF NOT EXISTS `books` (
  `booksid` int(11) NOT NULL AUTO_INCREMENT,
  `genre` varchar(8) NOT NULL,
  `title` varchar(100) NOT NULL,
  `author` varchar(30) NOT NULL,
  `publisher` varchar(30) NOT NULL,
  `year` int(4) NOT NULL,
  `price` varchar(11) NOT NULL,
  `summary` longtext NOT NULL,
  `description` longtext,
  PRIMARY KEY (`booksid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`booksid`, `genre`, `title`, `author`, `publisher`, `year`, `price`, `summary`, `description`) VALUES
(1, 'Foreign', 'Le Romant Comique (The Comic Novel): Vol I only of II', 'Paul Scarron', 'unknown, printed in Paris', 1668, '123', 'The Roman Comique tells the adventures of a company of strolling players in a realistic setting, thus providing much information concerning the customs of these companies of actors. The main plot tells the misadventures of Destin`s acting company and presents us with an intrusive narrator who disappears when some of the characters tell their life stories such as Destin, La Caverne and La Garouffiere. These stories have a higher tone than the main plot since they tell of past loves among the nobility. In the end the reader is able to unveil many of the mysteries of the novel and the relationship between the heroic material from the past and the comic adventures of the present since L`Estoile is the noble Leonore in disguise.\r\n\r\nThe novel also borrows some of its humor (partially embodied in Ragotin) from Miguel de Cervantes`s Don Quixote as well as from Menippean satire. It also contains four interpolated tales, taken mainly from Spanish sources. The most famous is the novella of the "Invisible Mistress," a comic adaptation of the more serious tale by Alonso de Castillo Solorzano. This tale, which includes a number of comic narrative intrusions, would be reworked by a number of English authors such as Thomas Otway and Eliza Haywood.', 'Hardcover, quarter leather. Astonishingly good condition for its age. All pages, all gilt, all leather almost miraculously intact. Pocket sized.'),
(2, 'Lit', 'Moby Dick', 'Herman Melville, Rockwell Kent', 'The Folio Society', 2009, '541', 'Herman Melville`s tale of the hunt for the white whale, Moby Dick, is one of the greatest novels of all time. It is at once an adventure story of the high seas, and an exploration of the uncharted regions of the soul. Neglected in Melvilleâ€™s day, Moby-Dick is now acknowledged as a sublime work of the imagination, an American Odyssey.\r\n\r\nâ€˜Call me Ishmaelâ€™ is one of the most famous opening sentences ever written. Moby-Dickâ€™s narrator Ishmael is a drifter and former merchant seaman, who departs Nantucket on a perilous whaling mission to the South Seas. With him on the Pequod are the cannibal Queequeg and â€˜a heathen crew â€¦ whelped somewhere by the sharkish seaâ€™. At the helm is Captain Ahab, whose soul is bent on hunting and killing the great white whale that cost him his leg in an earlier encounter. As they voyage south, Ahabâ€™s obsession takes his crew deeper into the abyss in desperate pursuit of â€˜the gliding great demon of the seas of lifeâ€™.\r\n', 'Hardcover, new. Each copy numbered by hand on a special limitation page. Bound in leather, blocked in white and silver with a design based on one of Rockwell Kent`s illustrations. Set in Fournier. 768 pages with approximately 280 black & white illustrations. Book Size: 9Â¼" x 6Â¾". Commentary volume is bound in buckram, 312 pages, 9Â¼" x 6Â¾". Both presented in a buckram-bound solander box.'),
(3, 'Bio', 'The RubÃ¡iyÃ¡t of Omar KhayyÃ¡m', 'Omar Khayyam, Edward Fitzgeral', 'Hodder & Stoughton', 1909, '294.00', 'Omar KhayyÃ¡m  (18 May 1048â€“1131) was a Persian mathematician, astronomer, philosopher and poet. He also wrote treatises on mechanics, geography, and music.\r\n\r\nBorn in Nishapur, at a young age he moved to Samarkand and obtained his education there, afterwards he moved to Bukhara and became established as one of the major mathematicians and astronomers of the medieval period. Recognized as the author of one of the most important treatises on algebra before modern times as reflected in his Treatise on Demonstration of Problems of Algebra giving a geometric method for solving cubic equations by intersecting a hyperbola with a circle. He contributed to a calendar reform.\r\n\r\nHis significance as a philosopher and teacher, and his few remaining philosophical works, have not received the same attention as his scientific and poetic writings. Zamakhshari referred to him as â€œthe philosopher of the worldâ€. Many sources have testified that he taught for decades the philosophy of Ibn Sina in Nishapur where KhayyÃ¡m was born and buried and where his mausoleum today remains a masterpiece of Iranian architecture visited by many people every year.\r\n', 'Hardcover, cloth, ornately decorated. Good. Some soiling and expected wear. Internally, some foxing. All illustrations tipped in, text and images all intact. A truly extraordinary book. Huge, 278 x 215mm, shipping overseas will cost more.'),
(4, 'Music', 'The Mysticism of Music, Sound and World', 'Hazrat Inayat Khan', 'Motilal Barnasidass Publishers', 2009, '19.00', 'The Mysticism of Music, Sound and Word presents and essential part of the highly practical philosophy of Hazart Inayat Khan. Born a musician, music and sound were for him the essence of life. Even when later he had to give up his practice of what in Indian philosophy is called struck music, the unstruck music remained with him, and increasingly so. Not only did he enjoy it in his meditations and all through life, whether in silent nature or in the roaring streets of Manhattan. In its fullness it came out in his discourses and other presentations he made to the amazed and yet not fully understanding public of the West in the twenties. His realization of sound and music transformed his words into living beings, attuning the atmosphere, bringing inspiration to the public.\r\n\r\nThis volume presents various aspects of his music. It contains four parts. In the Mysticism of Sound the basic philosophy is given in a wide context. Both the abstract and some very practical aspects are covered. Music presents the mystic aspects of music besides relating its philosophy to the practical side again. It presents a beautiful early view on Indian music and it relates music and colour. Moreover the psychic and healing powers are discussed. The Power of the Word and Cosmic Language elaborates these influences both philosophically and practically. The book really may be called mysticism in daily life.\r\n', ''),
(5, 'Poet', 'Publii Ovidii Nasonis Operum (Tomus III), in quo Fasti, Tristia, Ponticae Epistolae, et Ibis', 'Publius Ovidius Naso', 'Joannem Janssonium', 1662, '395.00', 'Contains Ovid`s poems written in exile on the Black Sea.\r\n\r\nContains Festivals, Lamentations, Letters from the Black Sea, and Curses. Book three of Ovid`s collected works.', 'Hardcover, quarter leather bound. Pocket sized, 4.5" x 3". In incredible condition for its age. Firmly bound, covers firmly attached, very sparingly read.'),
(6, 'Lit', 'The Ingoldsby Legends, or Mirth and Marvels', 'Richard Harris Barham, Crucksh', 'Frederick Warne & Co.', 1898, '75.00', 'The Ingoldsby Legends is a collection of myths, legends, ghost stories and poetry written supposedly by Thomas Ingoldsby of Tappington Manor, actually a pen-name of an English clergyman named Richard Harris Barham.\r\n\r\nThe legends were first printed during 1837 as a regular series in the magazine Bentley`s Miscellany and later in New Monthly Magazine. The legends were illustrated by John Leech and George Cruikshank. They proved immensely popular and were compiled into books published in 1840, 1842 and 1847 by Richard Bentley. They remained popular during the 19th century but have since become little known. An omnibus edition was published in 1879: The Ingoldsby Legends; or Mirth and marvels.\r\n\r\nAs a priest of the Chapel Royal, Barham was not troubled with strenuous duties and he had ample time to read and compose stories. Although based on real legends and mythology, such as the "hand of glory", they are mostly deliberately humorous parodies or pastiches of medieval folklore and poetry.\r\n\r\nThe collection contains one of the earliest transcriptions of the song A Franklyn`s Dogge, an early version of the modern children`s song Bingo. Other than this, the best-known poem of the collection is the Jackdaw of Rheims about a jackdaw who steals a cardinal`s ring and is made a saint.', 'Hardcover, full leather. Gilt on spine has worn off, and is sunned. Split to outer hinge repaired, but should be handled carefully. Considerable foxing. Otherwise firmly bound and sparingly read.'),
(7, 'Lit', 'The Book of Rustem, retold from the Shah Nameh of Firdausi', 'Firdausi, E. Wilmot Buxon', 'George Harrap & Co', 1907, '92.00', 'The nations of the world each have their respective culture, national heroes and in most cases, almost mythological epic stories. Just as the British have the King Arthurian legends, how Greece has its ancient Odyssey, and how the Americas have their pre-Columbus history; other countries have similar tales and legendary figures which have shaped their past. Most of these stories have many things in common. In particular, all of the heroes performed daring or glorious deeds to make their country an admirable place. This book of Persian tales is no different. It takes the reader on a fantastic journey â€” to a distant figment of the custom and culture of ancient Persian storytelling.', 'Hardcover, decorative cloth. In truly excellent condition for its age. Bright gilt, clean pages.'),
(8, 'Art', 'Swann''s Way', 'Marcel Proust, CK Scott Moncri', 'The Easton Press', 1982, '79.00', 'Swann''s Way, the first part, is one of the preeminent novels of childhood-a sensitive boy''s impressions of his family and neighbors, all brought dazzlingly back to life years later by the famous taste of a madeleine. It also enfolds the short novel Swann''s Love, an incomparable study of sexual jealousy, which becomes a crucial part of the vast, unfolding structure of In Search of Lost Time. The first volume of the book that established Proust as one of the finest voices of the modern age-satirical, skeptical, confiding, and endlessly varied in his response to the human condition-Swann''s Way also stands on its own as a perfect rendering of a life in art, of the past re-created through memory.', 'Hardcover, full leather. Good, with minor imperfections. A few small stains on back cover and edges. A huge and heavy book - shipping overseas will cost extra.'),
(9, 'Religion', 'The Master and the Margarita', 'Mikhail Boulgakov, Peter Suart', 'The Folio Society', 2005, '89.00', 'On a hot spring afternoon in Moscow, a poet and an editor are discussing the non-existence of Jesus. A polite, foreign gentleman interrupts their debate, claiming to have known Jesus in person and to have been present when he was condemned by Pontius Pilate. Moreover, he predicts the editorâ€™s death â€“ a bizarre accident which happens exactly as the foreigner foretells. The Devil has arrived in Moscow and, along with his demons and a large black cat, he carves a trail of chaos and destruction through Soviet society. He exposes the hypocrisy and greed of those around him, their willingness to inform on neighbours, their urgent scrabble for power and their fear for themselves. One man seems different: a writer known as â€˜the masterâ€™ who, in despair, has burned his unpublished novel about Pontius Pilate and has been incarcerated in an asylum. His lover, the passionate, courageous Margarita, will do anything to save him â€“ including serving the Devil himself.', 'Hardcover, decorative cloth with matching slipcase. New and perfect. Set in Joanna. 400 pages. Frontispiece. 12 colour illustrations. Book Size: 9Â½"Ã— 6Â¼".');

-- --------------------------------------------------------

--
-- Table structure for table `offers`
--

CREATE TABLE IF NOT EXISTS `offers` (
  `offersid` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(32) NOT NULL,
  `details` longtext NOT NULL,
  `submitdate` varchar(20) NOT NULL,
  PRIMARY KEY (`offersid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `offers`
--

INSERT INTO `offers` (`offersid`, `email`, `details`, `submitdate`) VALUES
(1, 'anthony@hotmail.com', 'Title: Common Sense / Rights of Man\r\nAuthor: Thomas Paine\r\nISBN: -\r\nPublisher: Classics of Liberty Library, private printing. 1992. Facsimile of the 1840 edition. Extremely scarce.\r\nCondition: Hardcover, leather spine with cloth boards. All edges gilt. With ribbon marker. Other than tiny dings on the edges, like new.', '2012-08-16 23:30:50'),
(2, 'penn@yahoo.com', 'Title: The Odyssey of Homer\r\nAuthor: Homer, Alexander Pope (trans), Buckley, Rev. Theodore Alois (Notes & Introduction)\r\nISBN: -\r\nPublisher: AL Burt, date unknown. Research reveals date to be no later than 1899.\r\nCondition: Hardcover, heavy leather spine and paper boards. Very nicely aged. Well bound, interior is excellent. Some water stains on cover, but looks unread.', '2012-08-16 23:31:16'),
(3, 'testing1234@sim.edu.sg', 'Title: The Princess / Maud  (volume 1), Enoch Arden / In Memoriam (volume 2)\r\nAuthor: Alfred Tennyson\r\nISBN: -\r\nPublisher: Macmillan, 1884\r\nCondition: Hardcover, quarter leather. In very good condition for its age. Nicely worn. Tightly bound.', '2012-08-16 23:31:45'),
(4, 'books@yahoo.com', 'Title: Two Treatises of Government\r\nAuthor: John Locke\r\nISBN: -\r\nPublisher: Classics of Liberty Library, private printing. 1992. Facsimile of the 1698 edition. Extremely scarce.\r\nCondition: Hardcover, leather spine with cloth boards. All edges gilt. With ribbon marker. Other than tiny dings on the edges, like new.', '2012-08-16 23:32:26'),
(5, 'books@yahoo.com', 'Title: Early Poems\r\n\r\nAuthor: Percy Bysshe Shelley\r\n\r\nPublisher: George Routledge & Sons, 1894\r\n\r\nISBN: -\r\n\r\nCondition: Hardcover boards. Some fraying to cover and a name inscription on fly page. Binding is tight, pages clean and unmarked. Excellent condition for its age.', '2012-08-16 23:32:41'),
(6, 'books@yahoo.com', 'Title: Confessions of an English Opium-Eater (Part I of The Works of Thomas De Quincey, Book XXIII of The World''s Classics)\r\n\r\nAuthor: Thomas De Quincey\r\n\r\nPublisher: London: Grant Richards, 1903\r\n\r\nISBN: -\r\n\r\nCondition: Hardcover, olive cloth boards. Decorative gold spine, as in picture. Watermark on back cover, and some sign of dirt. Other than that, no defect. Binding tight and pages clean.', '2012-08-16 23:32:55');

-- --------------------------------------------------------

--
-- Table structure for table `pictures`
--

CREATE TABLE IF NOT EXISTS `pictures` (
  `picid` int(11) NOT NULL AUTO_INCREMENT,
  `bookid` int(11) NOT NULL,
  `comment` longtext NOT NULL,
  `picture` text NOT NULL,
  PRIMARY KEY (`picid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=27 ;

--
-- Dumping data for table `pictures`
--

INSERT INTO `pictures` (`picid`, `bookid`, `comment`, `picture`) VALUES
(1, 1, 'Overview', 'uploadedimages/Foreign/3789Le_Romant_Comiqu_500d7cb6bc38e.jpg'),
(2, 1, 'Side view', 'uploadedimages/Foreign/6775romant-comique2.jpg'),
(3, 1, 'First page (1668)', 'uploadedimages/Foreign/9608romant-comique3.jpg'),
(4, 1, 'Another front page', 'uploadedimages/Foreign/6655romant-comique4.jpg'),
(5, 2, 'Whole Stack', 'uploadedimages/Lit/124Moby_Dick___Herm_5002923d2859c.jpg'),
(6, 2, 'First book', 'uploadedimages/Lit/7462moby-dick-folio2.jpg'),
(7, 2, 'Second Book', 'uploadedimages/Lit/1517moby-dick-folio3.jpg'),
(8, 2, 'Third Book', 'uploadedimages/Lit/3408moby-dick-folio4.jpg'),
(9, 3, 'Front View', 'uploadedimages/Bio/5904The_Rub__iy__t_o_500ba843ad5ea.jpg'),
(10, 3, 'Cover', 'uploadedimages/Bio/5364rubaiyat-dulac5.jpg'),
(11, 3, 'First Page', 'uploadedimages/Bio/6118rubaiyat-dulac7.jpg'),
(12, 3, 'Illustrations', 'uploadedimages/Bio/8147rubaiyat-dulac8.jpg'),
(13, 3, 'Cover', 'uploadedimages/Bio/6664rubaiyat-dulac9.jpg'),
(14, 3, 'More Illustrations', 'uploadedimages/Bio/5502rubaiyat-dulac10.jpg'),
(15, 4, 'Cover', 'uploadedimages/Music/5335The_Mysticism_of_50091f1b04174.jpg'),
(16, 5, 'Overview', 'uploadedimages/Poet/2355Poems_in_Exile_f_4fe6ecf8ac44b.jpg'),
(17, 5, 'First page', 'uploadedimages/Poet/4316ovid-1662-2.jpg'),
(18, 5, 'Pages of the book', 'uploadedimages/Poet/382ovid-1662-3.jpg'),
(19, 5, 'Close up of book binding', 'uploadedimages/Poet/7043ovid-1662-4.jpg'),
(20, 6, 'Overview', 'uploadedimages/Lit/8935The_Ingoldsby_Le_4fe88f18294b5.jpg'),
(21, 6, 'Page in the book', 'uploadedimages/Lit/6614ingoldsby-legends2.jpg'),
(22, 6, 'Front page', 'uploadedimages/Lit/2926ingoldsby-legends3.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `vendors`
--

CREATE TABLE IF NOT EXISTS `vendors` (
  `vendorid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(25) NOT NULL,
  `cpwd` varchar(32) NOT NULL,
  PRIMARY KEY (`vendorid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `vendors`
--

INSERT INTO `vendors` (`vendorid`, `username`, `cpwd`) VALUES
(1, 'BooksCollector', '1cd8e7658bb6db26fed1ce17940b7dbd'),
(2, 'Books4Us', 'd8578edf8458ce06fbc5bb76a58c5ca4'),
(3, 'Wherebooks', '0e8517ecab65b546e3d9764d6f5924c3'),
(4, 'pkloo', '75c7e04bd69ce636f3d9432edd111a52');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
