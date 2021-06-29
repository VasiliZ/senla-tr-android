package com.github.rtyvz.senla.tr.dbapp.provider

import com.github.rtyvz.senla.tr.dbapp.db.AppDb
import com.github.rtyvz.senla.tr.dbapp.models.CommentEntity
import com.github.rtyvz.senla.tr.dbapp.models.PostEntity
import com.github.rtyvz.senla.tr.dbapp.models.UserEntity

object DbDataProvider {

    fun populateDB(db: AppDb) {
        db.populateTable(
            listOf(
                UserEntity(name = "Kazuo", lastName = "Bertram", email = "kbertram@domain.tld"),
                UserEntity(name = "Goronwy", lastName = "Gruffydd", email = "ggruffydd@domain.tld"),
                UserEntity(name = "Celestino", lastName = "Marius", email = "cmarius@domain.tld"),
                UserEntity(name = "Selma", lastName = "Davorka", email = "sdavorka@domain.tld"),
                UserEntity(name = "Keelin", lastName = "Renata", email = "krenata@domain.tld")
            )
        )

        db.populateTable(
            listOf(
                PostEntity(
                    userId = 1,
                    title = "When Growths Send You Running for Cover",
                    body = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, ",
                    rate = 3
                ),
                PostEntity(
                    userId = 4,
                    title = "Warning: You're Losing Money by Not Using Customer Services",
                    body = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur? At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere",
                    rate = 4
                ),
                PostEntity(
                    userId = 5,
                    title = "Why the Next 10 Years of Your Opinions Will Smash the Last 10",
                    body = "Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild Question Marks and devious Semikoli, but the Little Blind Text didn’t listen. She packed her seven versalia, put her initial into the belt and made herself on the way. When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her hometown Bookmarksgrove, the headline of Alphabet Village and the subline of her own road, the Line Lane. Pityful a rethoric question ran over her cheek, then ",
                    rate = 5
                ),
                PostEntity(
                    userId = 1,
                    title = "What Jezebel Should Write About The Customer Experience",
                    body = "One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections. The bedding was hardly able to cover it and seemed ready to slide off any moment. His many legs, pitifully thin compared with the size of the rest of him, waved about helplessly as he looked. \"\"What's happened to me?\"\" he thought. It wasn't a dream. His room, a proper human room although a little too small, lay peacefully between its four familiar walls. A collection of textile samples lay spread out on the table - Samsa was a travelling salesman - and above it there hung a picture that he had recently cut out of an illustrated magazine and housed in a nice, gilded frame. It showed a lady fitted out with a fur hat and fur boa who sat upright, raising a heavy fur muff that covered the whole of her lower arm towards the viewer. Gregor then turned to look out the window at the dull weather. Drops",
                    rate = 1
                ),
                PostEntity(
                    userId = 2,
                    title = "The Dummies' Guide to A Challenge",
                    body = "A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so happy, my dear friend, so absorbed in the exquisite sense of mere tranquil existence, that I neglect my talents. I should be incapable of drawing a single stroke at the present moment; and yet I feel that I never was a greater artist than now. When, while the lovely valley teems with vapour around me, and the meridian sun strikes the upper surface of the impenetrable foliage of my trees, and but a few stray gleams steal into the inner sanctuary, I throw myself down among the tall grass by the trickling stream; and, as I lie close to the earth, a thousand unknown plants are noticed by me: when I hear the buzz of the little world among the stalks, and grow familiar with the countless indescribable forms of the insects and flies, then I feel the presence of the Almighty, who formed us in his own image, and the breath",
                    rate = 4
                )
            )
        )

        db.populateTable(
            listOf(
                CommentEntity(
                    postId = 1,
                    userId = 1,
                    text = "This shot blew my mind."
                ),
                CommentEntity(
                    postId = 2,
                    userId = 2,
                    text = "It's amazing not just slick!"
                ),
                CommentEntity(
                    postId = 1,
                    userId = 3,
                    text = "Immensely thought out! Congrats on the new adventure!!"
                ),
                CommentEntity(
                    postId = 4,
                    userId = 4,
                    text = "Revolutionary. I approve the use of iconography and gradient!"
                ),
                CommentEntity(
                    postId = 3,
                    userId = 2,
                    text = "Lavender. I'd love to see a video of how it works."
                )
            )
        )
    }
}